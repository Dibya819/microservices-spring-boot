package com.dibya.programming.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TrafficSignalStatusEvent {

    private String city;
    private String area;
    private String status;
    private LocalDateTime timestamp;

}
