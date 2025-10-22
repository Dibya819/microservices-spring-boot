package com.dibya.programming.service;

import com.dibya.programming.dtos.ViolationRequestDTO;
import com.dibya.programming.dtos.ViolationResponseDTO;
import com.dibya.programming.dtos.ViolationUpdateRequestDTO;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ViolationService {
    CompletableFuture<ViolationResponseDTO> addViolation(Long officerId, ViolationRequestDTO requestDTO, String jwtToken);

    List<ViolationResponseDTO> getViolationsByDriver(String email,String phoneNumber,String vehicleNumber);

    ViolationResponseDTO updateViolation(String email, String phoneNumber, String vehicleNumber, String violationType,
                                         String timestamp, ViolationUpdateRequestDTO requestDTO);

    void deleteViolation(String email, String phoneNumber, String vehicleNumber, String violationType,
                         String timestamp);
}
