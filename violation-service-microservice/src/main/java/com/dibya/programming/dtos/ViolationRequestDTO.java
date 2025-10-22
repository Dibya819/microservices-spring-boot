package com.dibya.programming.dtos;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationRequestDTO {

    @NotBlank(message = "Vehicle number is required")
    private String vehicleNumber;

    @NotNull(message = "Violation type is required")
    private String violationType;

    @Positive(message = "Fine amount must be greater than 0")
    private double fineAmount;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Timestamp is required")
    private LocalDateTime timestamp;
}
