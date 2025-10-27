package com.dibya.programming.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentEventDTO {
    private Long userId;
    private Double amount;
    private String paymentStatus;
    private String paymentId;
    private String orderId;
}
