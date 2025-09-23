package com.dibya.programming.service;

import com.dibya.programming.dtos.VehicleRequestDto;
import com.dibya.programming.dtos.UpdateVehicleDTO;
import com.dibya.programming.dtos.VehicleResponseDto;

import java.util.List;

public interface VehicleService {
    VehicleResponseDto createVehicle(VehicleRequestDto requestDto, Long ownerId);
    VehicleResponseDto updateVehicle(Long id, UpdateVehicleDTO updateDto, Long newOwnerId);
    List<VehicleResponseDto> getVehiclesByOwner(Long ownerId);
    void deleteVehicleOfUser(Long vehicleId, Long ownerId);
}
