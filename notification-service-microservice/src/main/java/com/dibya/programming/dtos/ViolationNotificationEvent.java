package com.dibya.programming.dtos;

import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationNotificationEvent {
    private String userEmail;
    private String userPhone;
    private String userName;
    private String violationType;
    private double fineAmount;
    private String location;
    private LocalDateTime timestamp;
    private String vehicleRegistrationNumber;
    private String vehicleType;
}
