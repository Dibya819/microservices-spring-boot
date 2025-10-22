package com.dibya.programming.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationResponseDTO {

    private Long userId;
    private String vehicleNumber;
    private String vehicleType;

    private String userName;
    private String userEmail;
    private String userPhone;

    private String violationType;
    private double fineAmount;
    private String location;
    private LocalDateTime timestamp;
    private double totalFine;
}
