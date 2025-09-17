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
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {
    @NotBlank(message = "Name can not be blank")
    private String name;
    @Email(message = "Invalid Email Type!!!")
    private String email;
    @NotBlank(message = "Password Required!!!")
    private String password;
    @Pattern(regexp="DRIVER|TRAFFIC_OFFICER|ADMIN", message="Role must be valid")
    private String role;
    @NotBlank
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    private String phoneNumber;
    @NotBlank(message = "Address can not be blank")
    private String address;
    @NotBlank(message = "State can not be blank")
    private String state;
    @Pattern(regexp = "^[0-9]{6}$", message = "Pincode must be 6 digits")
    private String pincode;

}
