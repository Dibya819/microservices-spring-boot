package com.dibya.programming.dtos;

import lombok.*;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class PaymentRequestDTO {
        private String currency; // default "INR"
    }


