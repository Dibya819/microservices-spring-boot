package com.dibya.programming.controller;

import com.dibya.programming.dtos.PaymentRequestDTO;
import com.dibya.programming.dtos.PaymentResponseDTO;
import com.dibya.programming.dtos.PaymentVerificationDTO;
import com.dibya.programming.service.PaymentServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentServiceImpl paymentService;

    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create")
    public ResponseEntity<PaymentResponseDTO> createPayment(@RequestHeader("X-User-Id") Long userId,
                                                            @RequestHeader("Authorization") String authHeader,
                                                            @Valid @RequestBody PaymentRequestDTO requestDTO){
        PaymentResponseDTO responseDTO=paymentService.createPaymentOrder(userId,requestDTO,authHeader);
        HttpStatus status="CREATED".equals(responseDTO.getStatus()) ? HttpStatus.CREATED : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(responseDTO,status);
    }
    @PostMapping("/verify")
    public ResponseEntity<PaymentResponseDTO> verifyPayment(@RequestHeader("X-User-Id") Long userId,
                                                            @RequestHeader("Authorization") String authHeader,
                                                            @Valid @RequestBody PaymentVerificationDTO verificationDTO){
        PaymentResponseDTO response = paymentService.verifyPayment(userId, verificationDTO,authHeader);
        HttpStatus status = "SUCCESS".equals(response.getStatus()) ? HttpStatus.OK : HttpStatus.BAD_REQUEST;
        return new ResponseEntity<>(response, status);
    }

}
