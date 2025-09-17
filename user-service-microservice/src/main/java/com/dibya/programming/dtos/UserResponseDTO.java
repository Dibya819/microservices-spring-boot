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
public class UserResponseDTO {
    private Long id;
    private String name;
    private String email;
    private String role;
    private String phoneNumber;
    private String address;
    private String state;
    private String pincode;
    private LocalDateTime createdAt;
}
