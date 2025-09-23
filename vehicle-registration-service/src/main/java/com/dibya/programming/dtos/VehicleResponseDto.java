package com.dibya.programming.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleResponseDto {
    private Long id;
    private String registrationNumber;
    private String type;
    private Long ownerId;
    private String model;
    private String color;
    private String email;
    private String number;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
