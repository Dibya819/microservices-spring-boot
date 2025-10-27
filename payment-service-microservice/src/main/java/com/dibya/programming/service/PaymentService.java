package com.dibya.programming.service;

import com.dibya.programming.dtos.PaymentRequestDTO;
import com.dibya.programming.dtos.PaymentResponseDTO;
import com.dibya.programming.dtos.PaymentVerificationDTO;

public interface PaymentService {
    PaymentResponseDTO createPaymentOrder (Long userId, PaymentRequestDTO requestDTO,String jwtToken);
    PaymentResponseDTO verifyPayment(Long userId, PaymentVerificationDTO verificationDTO,String jwtToken);
}
