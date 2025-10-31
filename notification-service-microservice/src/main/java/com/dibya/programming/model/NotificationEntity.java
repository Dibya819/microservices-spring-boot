package com.dibya.programming.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private String violationType;
    private String vehicleRegistrationNumber;
    private String vehicleType;
    private double fineAmount;
    private String location;
    private LocalDateTime timestamp;

    private boolean emailSent;

    private LocalDateTime createdAt;
}
