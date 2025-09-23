package com.dibya.programming.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VehicleRequestDto {
    @Size(min = 6, max = 15, message = "Registration number must be between 6 and 15 characters")
    @NotBlank(message = "Registration number is required")
    private String registrationNumber;

    @NotBlank(message = "Type is required")
    @Pattern(regexp = "CAR|BIKE|TRUCK|BUS", message = "Type must be CAR, BIKE, TRUCK, or BUS")
    private String type;

    @NotBlank(message = "Model cannot be blank")
    @Size(max = 50, message = "Model name should not exceed 50 characters")
    private String model;

    @NotBlank(message = "Color cannot be blank")
    @Size(max = 20, message = "Color name should not exceed 20 characters")
    private String color;

    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "\\+?[0-9]{7,15}", message = "Invalid phone number")
    private String phoneNumber;
}
