package com.dibya.programming.service;

import com.dibya.programming.dtos.UpdateVehicleDTO;
import com.dibya.programming.dtos.VehicleRequestDto;
import com.dibya.programming.dtos.VehicleResponseDto;
import com.dibya.programming.globalexceptionhandling.DuplicateVehicleException;
import com.dibya.programming.globalexceptionhandling.VehicleNotFoundException;
import com.dibya.programming.model.Vehicles;
import com.dibya.programming.repository.VehicleRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class VehicleServiceImpl implements VehicleService{

    private final VehicleRepository vehicleRepository;
    private final WebClient webClient;

    @Autowired
    public VehicleServiceImpl(VehicleRepository vehicleRepository, WebClient.Builder webClientBuilder) {
        this.vehicleRepository = vehicleRepository;
        this.webClient = webClientBuilder.build();
    }

    @Override
    public VehicleResponseDto createVehicle(VehicleRequestDto requestDto, Long ownerId) {
        if(vehicleRepository.existsByRegistrationNumber(requestDto.getRegistrationNumber())) {
            throw new DuplicateVehicleException(requestDto.getRegistrationNumber());
        }
        Vehicles vehicles= mapToEntity(requestDto,ownerId);
        Vehicles savedVehicles=vehicleRepository.save(vehicles);
      return mapToDto(savedVehicles);
    }

    @Override
    public VehicleResponseDto updateVehicle(Long id, UpdateVehicleDTO updateDto, Long newOwnerId) {
        Vehicles ExistingVehicles=vehicleRepository.findById(id).orElseThrow(()->new VehicleNotFoundException(id));
        if(vehicleRepository.existsByRegistrationNumber(updateDto.getRegistrationNumber())
                && !updateDto.getRegistrationNumber().equals( ExistingVehicles.getRegistrationNumber())) {
            throw new DuplicateVehicleException(updateDto.getRegistrationNumber());
        }
        mergeVehicleData(ExistingVehicles,updateDto,newOwnerId);
        Vehicles savedVehicle=vehicleRepository.save(ExistingVehicles);
        return mapToDto(savedVehicle);
    }

    @Override
    public List<VehicleResponseDto> getVehiclesByOwner(Long ownerId) {
     List<Vehicles> vehicles=vehicleRepository.findByOwnerId(ownerId);
     return vehicles.stream().map(this::mapToDto).toList();
    }

    @Override
    public void deleteVehicleOfUser(Long vehicleId, Long ownerId) {
       Vehicles vehicles=vehicleRepository.findById(vehicleId).orElseThrow(()->new VehicleNotFoundException(vehicleId));
       if(!vehicles.getOwnerId().equals(ownerId)){
           throw new RuntimeException("Vehicle does not belong to the specified user");
       }
       vehicleRepository.delete(vehicles);
    }

    @Override
    public VehicleResponseDto getVehicleByRegistrationNumber(String registrationNumber) {
        Vehicles vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found with registration number: " + registrationNumber));
        return mapToDto(vehicle);
    }


    private Vehicles mapToEntity(VehicleRequestDto requestDto,Long ownerId){
        return Vehicles.builder()
                .registrationNumber(requestDto.getRegistrationNumber())
                .type(requestDto.getType())
                .model(requestDto.getModel())
                .color(requestDto.getColor())
                .email(requestDto.getEmail())
                .number(requestDto.getPhoneNumber())
                .ownerId(ownerId)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    private VehicleResponseDto mapToDto(Vehicles vehicle){
        return VehicleResponseDto.builder()
                .id(vehicle.getId())
                .registrationNumber(vehicle.getRegistrationNumber())
                .type(vehicle.getType())
                .ownerId(vehicle.getOwnerId())
                .model(vehicle.getModel())
                .color(vehicle.getColor())
                .email(vehicle.getEmail())
                .number(vehicle.getNumber())
                .createdAt(vehicle.getCreatedAt())
                .updatedAt(vehicle.getUpdatedAt())
                .build();
    }
    private void mergeVehicleData(Vehicles existingVehicle, UpdateVehicleDTO dto, Long newOwnerId) {
        if (dto.getRegistrationNumber() != null) existingVehicle.setRegistrationNumber(dto.getRegistrationNumber());
        if (dto.getType() != null) existingVehicle.setType(dto.getType());
        if (dto.getModel() != null) existingVehicle.setModel(dto.getModel());
        if (dto.getColor() != null) existingVehicle.setColor(dto.getColor());
        if (newOwnerId != null) existingVehicle.setOwnerId(newOwnerId);
        if (dto.getEmail() != null) existingVehicle.setEmail(dto.getEmail());
        if (dto.getPhoneNumber() != null) existingVehicle.setNumber(dto.getPhoneNumber());
        existingVehicle.setUpdatedAt(LocalDateTime.now());
    }

    @CircuitBreaker(name = "userServiceCircuit", fallbackMethod = "getUserIdFallback")
    @Retry(name = "userServiceRetry", fallbackMethod = "getUserIdFallback")
    @RateLimiter(name = "userServiceRateLimiter", fallbackMethod = "getUserIdFallback")
    @TimeLimiter(name = "userServiceTimeLimiter")
    public CompletableFuture<Long> getUserId(String email,String phoneNumber){
        UriComponentsBuilder uriBuilder = UriComponentsBuilder
                .fromHttpUrl("http://api-gateway/auth/id");
        if (email != null) uriBuilder.queryParam("email", email);
        if (phoneNumber != null) uriBuilder.queryParam("phoneNumber", phoneNumber);

        return webClient.get()
                .uri(uriBuilder.toUriString())
                .retrieve()
                .bodyToMono(Long.class)
                .toFuture();
    }
    private CompletableFuture<Long> getUserIdFallback(String email, String phoneNumber, Throwable t) {
        System.out.println("Fallback triggered due to: " + t.getMessage());
        return CompletableFuture.failedFuture(
                new RuntimeException("Service temporarily unavailable. Please try again after some time.")
        );
    }

}
