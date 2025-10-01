package com.dibya.programming.dtos;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTrafficSignalRequestDto {

    @Pattern(regexp = "RED|GREEN|YELLOW", message = "Status must be RED, GREEN, or YELLOW")
    private String status;

}
