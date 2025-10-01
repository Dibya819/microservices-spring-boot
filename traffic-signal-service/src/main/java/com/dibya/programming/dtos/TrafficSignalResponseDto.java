package com.dibya.programming.dtos;


import com.dibya.programming.model.TrafficSignal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficSignalResponseDto {
    private Long id;

    private String city;
    private String state;
    private String area;
    private String pincode;

    private String status;

    private Boolean manualOverride;

    private String metadata;

    private LocalDateTime lastUpdated;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
