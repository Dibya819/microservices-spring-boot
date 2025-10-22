package com.dibya.programming.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViolationUpdateRequestDTO {

    @NotNull(message = "Violation type is required")
    private String violationType;

    @Positive(message = "Fine amount must be greater than 0")
    private double fineAmount;

    @NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Timestamp is required")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;
}
