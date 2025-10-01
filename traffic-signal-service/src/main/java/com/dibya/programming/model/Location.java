package com.dibya.programming.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @jakarta.persistence.Column(nullable = false)
    private String city;

    @jakarta.persistence.Column(nullable = false)
    private String state;

    @jakarta.persistence.Column(nullable = false)
    private String area;

    @jakarta.persistence.Column(nullable = false)
    private String pincode;
}
