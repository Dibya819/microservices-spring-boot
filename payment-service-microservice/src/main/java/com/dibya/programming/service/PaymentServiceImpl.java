package com.dibya.programming.service;

import com.dibya.programming.dtos.PaymentRequestDTO;
import com.dibya.programming.dtos.PaymentResponseDTO;
import com.dibya.programming.dtos.PaymentVerificationDTO;
import com.dibya.programming.model.Payment;
import com.dibya.programming.repository.PaymentRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final WebClient webClient;

    @Value("${razorpay.key-id}")
    private String razorpayKeyId;

    @Value("${razorpay.key-secret}")
    private String razorpayKeySecret;

    public PaymentServiceImpl(PaymentRepository paymentRepository, WebClient.Builder webClient) {
        this.paymentRepository = paymentRepository;
        this.webClient = webClient.build();
    }

    @Override
    @CircuitBreaker(name = "paymentServiceCircuit", fallbackMethod = "addPaymentFallback")
    @Retry(name = "paymentServiceRetry", fallbackMethod = "addPaymentFallback")
    @RateLimiter(name = "paymentServiceRateLimiter", fallbackMethod = "addPaymentFallback")
    @TimeLimiter(name = "paymentServiceTimeLimiter")
    public PaymentResponseDTO createPaymentOrder(Long userId, PaymentRequestDTO requestDTO,String jwtToken) {

        try {

            RazorpayClient client = new RazorpayClient(razorpayKeyId, razorpayKeySecret);


            JSONObject options = new JSONObject();
            Double totalFine = webClient.get()
                    .uri("http://api-gateway/violations/total-fine")
                    .header("Authorization", jwtToken)
                    .header("X-User-Id", String.valueOf(userId))
                    .header("X-User-Role", "DRIVER")
                    .retrieve()
                    .bodyToMono(Double.class)
                    .block();

            if (totalFine == null || totalFine <= 0) {
                return PaymentResponseDTO.builder()
                        .status("FAILED")
                        .message("No pending fines found for this user.")
                        .build();
            }

            long amountInPaise = (long) (totalFine * 100);
            options.put("amount", amountInPaise);
            options.put("currency", requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "INR");
            options.put("payment_capture", 1);


            Order order = client.orders.create(options);


            Payment payment = Payment.builder()
                    .userId(userId)
                    .amount(totalFine)
                    .orderId(order.get("id"))
                    .status("CREATED")
                    .createdAt(LocalDateTime.now())
                    .build();
            paymentRepository.save(payment);

            return PaymentResponseDTO.builder()
                    .orderId(order.get("id"))
                    .amount(totalFine)
                    .currency(requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "INR")
                    .status("CREATED")
                    .message("Order created successfully")
                    .build();

        } catch (RazorpayException e) {
            return PaymentResponseDTO.builder()
                    .status("FAILED")
                    .message("Error creating order: " + e.getMessage())
                    .build();
        }
    }
    public PaymentResponseDTO addPaymentFallback(Long userId, PaymentRequestDTO requestDTO, String jwtToken, Throwable throwable) {

        String errorMessage = "Payment service is currently unavailable. Please try again later.";

        if (throwable != null) {
            System.err.println("Fallback triggered due to: " + throwable.getMessage());
        }

        return PaymentResponseDTO.builder()
                .status("FAILED")
                .message(errorMessage)
                .amount(0.0)
                .currency(requestDTO.getCurrency() != null ? requestDTO.getCurrency() : "INR")
                .orderId(null)
                .build();
    }

    @Override
    public PaymentResponseDTO verifyPayment(Long userId, PaymentVerificationDTO verificationDTO,String jwtToken) {
        try {
            String payload = verificationDTO.getRazorpayOrderId() + "|" + verificationDTO.getRazorpayPaymentId();

            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(razorpayKeySecret.getBytes(), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] hash = mac.doFinal(payload.getBytes());
            String generatedSignature = Base64.getEncoder().encodeToString(hash);

            boolean isValid = generatedSignature.equals(verificationDTO.getRazorpaySignature());

            Payment payment = paymentRepository.findByOrderId(verificationDTO.getRazorpayOrderId());
            if (payment == null) {
                return PaymentResponseDTO.builder()
                        .status("FAILED")
                        .message("Order not found in DB")
                        .build();
            }

            if (isValid) {
                payment.setPaymentId(verificationDTO.getRazorpayPaymentId());
                payment.setStatus("SUCCESS");
                payment.setUpdatedAt(LocalDateTime.now());
                paymentRepository.save(payment);

                webClient.post()
                        .uri("http://api-gateway/violations/clear-fines")
                        .header("Authorization", jwtToken)
                        .header("X-User-Id", String.valueOf(userId))
                        .header("X-User-Role", "USER")
                        .retrieve()
                        .bodyToMono(Void.class)
                        .block();

                return PaymentResponseDTO.builder()
                        .orderId(payment.getOrderId())
                        .amount(payment.getAmount())
                        .currency("INR")
                        .status("SUCCESS")
                        .message("Payment verified successfully")
                        .build();
            } else {
                payment.setStatus("FAILED");
                paymentRepository.save(payment);

                return PaymentResponseDTO.builder()
                        .orderId(payment.getOrderId())
                        .status("FAILED")
                        .message("Invalid payment signature")
                        .build();
            }

        } catch (Exception e) {
            return PaymentResponseDTO.builder()
                    .status("FAILED")
                    .message("Error verifying payment: " + e.getMessage())
                    .build();
        }
    }
}
