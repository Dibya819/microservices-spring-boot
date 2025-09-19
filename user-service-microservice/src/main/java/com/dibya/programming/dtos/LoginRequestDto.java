package com.dibya.programming.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {
    @Email(message = "Email Format is invalid")
    @NotBlank(message = "Please insert the Email")
    private String Email;
    @NotBlank(message = "Password field is required")
    private String password;

}
