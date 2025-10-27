package com.dibya.programming.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDTO {

    private String orderId;
    private Double amount;
    private String currency;
    private String status;
    private String message;

}
