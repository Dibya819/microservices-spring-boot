package com.dibya.programming.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRequestDto {

    private String name;

    @Email(message = "Invalid Email format")
    private String email;

    private String password;

    @Pattern(regexp = "DRIVER|TRAFFIC_OFFICER|ADMIN", message = "Role must be valid")
    private String role;

    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;

    private String address;
    private String state;
    private String pincode;
}
