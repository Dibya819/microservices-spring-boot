package com.dibya.programming.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String role;
}
