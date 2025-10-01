package com.dibya.programming.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficSignalRequestDto {
    @NotBlank(message = "City is required")
    private String city;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "Area is required")
    private String area;

    @NotBlank(message = "Pincode is required")
    private String pincode;

    @Size(max = 500, message = "Metadata must be at most 500 characters")
    private String metadata;

    private String status;

}
