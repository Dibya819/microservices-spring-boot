package com.dibya.programming.dtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleInfoDTO {
    private Long id;
    private String registrationNumber;
    private String type;
    private Long ownerId;

}
