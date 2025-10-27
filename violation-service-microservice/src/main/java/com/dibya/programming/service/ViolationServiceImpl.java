package com.dibya.programming.service;

import com.dibya.programming.Enum.ViolationType;
import com.dibya.programming.dtos.*;
import com.dibya.programming.globalexceptionhandling.UserNotFoundException;
import com.dibya.programming.globalexceptionhandling.VehicleNotFoundException;
import com.dibya.programming.globalexceptionhandling.ViolationNotFoundException;
import com.dibya.programming.model.UserInfo;
import com.dibya.programming.model.VehicleInfo;
import com.dibya.programming.model.Violation;
import com.dibya.programming.repository.UserInfoRepository;
import com.dibya.programming.repository.VehicleInfoRepository;
import com.dibya.programming.repository.ViolationRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
public class ViolationServiceImpl implements ViolationService{

    private final ViolationRepository repository;
    private final WebClient webClient;
    private final UserInfoRepository userInfoRepository;
    private final VehicleInfoRepository vehicleInfoRepository;

    @Autowired
    public ViolationServiceImpl(ViolationRepository repository, WebClient.Builder webClient, UserInfoRepository userInfoRepository, VehicleInfoRepository vehicleInfoRepository) {
        this.repository = repository;
        this.webClient = webClient.build();
        this.userInfoRepository = userInfoRepository;
        this.vehicleInfoRepository = vehicleInfoRepository;
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "violationServiceCircuit", fallbackMethod = "addViolationFallback")
    @Retry(name = "violationServiceRetry", fallbackMethod = "addViolationFallback")
    @RateLimiter(name = "violationServiceRateLimiter", fallbackMethod = "addViolationFallback")
    @TimeLimiter(name = "violationServiceTimeLimiter")
    public CompletableFuture<ViolationResponseDTO> addViolation(Long officerId, ViolationRequestDTO requestDTO, String jwtToken) {
        return CompletableFuture.supplyAsync(() -> {
            VehicleInfoDTO vehicleInfoDTO = webClient.get()
                    .uri("http://api-gateway/vehicles/details/{registrationNumber}", requestDTO.getVehicleNumber())
                    .header("Authorization", jwtToken)
                    .header("X-User-Id", String.valueOf(officerId))
                    .header("X-User-Role", "TRAFFIC_OFFICER")
                    .retrieve()
                    .bodyToMono(VehicleInfoDTO.class)
                    .block();

            if (vehicleInfoDTO == null) {
                throw new VehicleNotFoundException("Vehicle not found with registration number: " + requestDTO.getVehicleNumber());
            }

            UserInfoDTO userDTO = webClient.get()
                    .uri("http://api-gateway/users/{id}", vehicleInfoDTO.getOwnerId())
                    .header("Authorization", jwtToken)
                    .header("X-User-Id", String.valueOf(officerId))
                    .header("X-User-Role", "TRAFFIC_OFFICER")
                    .retrieve()
                    .bodyToMono(UserInfoDTO.class)
                    .block();

            if (userDTO == null) {
                throw new UserNotFoundException("User not found with ID: " + vehicleInfoDTO.getOwnerId());
            }

            VehicleInfo vehicleInfo = vehicleInfoRepository.findVehicleByVehicleId(vehicleInfoDTO.getId())
                    .orElseGet(() -> vehicleInfoRepository.save(mapVehicleDtoToEntity(vehicleInfoDTO)));

            UserInfo userInfo = userInfoRepository.findUserByUserId(userDTO.getId())
                    .orElseGet(() -> userInfoRepository.save(mapUserDtoToEntity(userDTO)));

            userInfo.setTotalFine(userInfo.getTotalFine() + requestDTO.getFineAmount());
            userInfoRepository.save(userInfo);

            Violation violation = mapToEntity(requestDTO, vehicleInfo, userInfo);
            Violation savedViolation = repository.save(violation);

            return mapToDto(savedViolation);
        });
    }


    public ViolationResponseDTO addViolationFallback(Long officerId, ViolationRequestDTO requestDTO, String jwtToken, Throwable t) {
        System.err.println("⚠️ Fallback triggered for addViolation due to: " + t.getMessage());

        return ViolationResponseDTO.builder()
                .vehicleNumber(requestDTO.getVehicleNumber())
                .violationType(requestDTO.getViolationType())
                .fineAmount(requestDTO.getFineAmount())
                .location(requestDTO.getLocation())
                .timestamp(LocalDateTime.now())
                .userName("Unavailable")
                .userEmail("Unavailable")
                .userPhone("Unavailable")
                .totalFine(0.0)
                .build();
    }


    @Override
    public List<ViolationResponseDTO> getViolationsByDriver(String email, String phoneNumber,String vehicleNumber) {

        UserInfo user = null;
        VehicleInfo vehicle = null;

        if (email != null && !email.isBlank() || phoneNumber != null && !phoneNumber.isBlank()) {
            user = userInfoRepository.findByEmailOrPhoneNumber(email, phoneNumber)
                    .orElseThrow(() -> new UserNotFoundException("User not found"));
        }

        if (vehicleNumber != null && !vehicleNumber.isBlank()) {
            vehicle = vehicleInfoRepository.findByVehicleNumber(vehicleNumber)
                    .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));
        }

        List<Violation>violations;

        if (user != null && vehicle != null) {
            violations = repository.findByUserInfoAndVehicleInfo(user, vehicle);
        } else if (user != null) {
            violations = repository.findByUserInfo(user);
        } else if (vehicle != null) {
            violations = repository.findByVehicleInfo(vehicle);
        } else {
            throw new IllegalArgumentException("Invalid input");
        }
        return violations.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ViolationResponseDTO updateViolation(String email, String phoneNumber, String vehicleNumber, String violationType, String timestamp, ViolationUpdateRequestDTO requestDTO) {

        UserInfo user = email != null ? userInfoRepository.findByEmail(email)
                : userInfoRepository.findByPhoneNumber(phoneNumber);
        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        VehicleInfo vehicle = vehicleInfoRepository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

        LocalDateTime violationTime = timestamp != null ? LocalDateTime.parse(timestamp.trim(), DateTimeFormatter.ISO_DATE_TIME)
                : null;

        Optional<Violation> optionalViolation = repository.findByUserInfoAndVehicleInfoAndViolationTypeAndTimestamp(
                user, vehicle, ViolationType.valueOf(violationType.toUpperCase()), violationTime
        );

        Violation violation = optionalViolation.orElseThrow(() -> new ViolationNotFoundException("Violation not found"));

        user.setTotalFine(user.getTotalFine() - violation.getFineAmount() + requestDTO.getFineAmount());
        userInfoRepository.save(user);

        violation.setFineAmount(requestDTO.getFineAmount());
        violation.setLocation(requestDTO.getLocation());
        violation.setViolationType(ViolationType.valueOf(requestDTO.getViolationType().toUpperCase()));
        violation.setTimestamp(requestDTO.getTimestamp() != null ? requestDTO.getTimestamp() : violation.getTimestamp());

        Violation updated = repository.save(violation);

        return mapToDto(updated);

    }

    @Override
    @Transactional
    public void deleteViolation(String email, String phoneNumber, String vehicleNumber,
                                String violationType, String timestamp) {

        UserInfo user = email != null ? userInfoRepository.findByEmail(email)
                : userInfoRepository.findByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }

        VehicleInfo vehicle = vehicleInfoRepository.findByVehicleNumber(vehicleNumber)
                .orElseThrow(() -> new VehicleNotFoundException("Vehicle not found"));

        LocalDateTime violationTime = timestamp != null ? LocalDateTime.parse(timestamp.trim(), DateTimeFormatter.ISO_DATE_TIME)
                : null;

        Optional<Violation> optionalViolation =repository.findByUserInfoAndVehicleInfoAndViolationTypeAndTimestamp(
                user, vehicle, ViolationType.valueOf(violationType.toUpperCase()), violationTime
        );

        Violation violation = optionalViolation.orElseThrow(() -> new ViolationNotFoundException("Violation not found"));

        user.setTotalFine(user.getTotalFine() - violation.getFineAmount());
        userInfoRepository.save(user);

        repository.delete(violation);
    }

    public double getTotalFineForUser(Long userId) {
        return userInfoRepository.findUserByUserId(userId)
                .map(UserInfo::getTotalFine)
                .orElse(0.0);
    }

    @Transactional
    public void clearUserFines(Long userId) {
        UserInfo user = userInfoRepository.findUserByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setTotalFine(0.0);
        userInfoRepository.save(user);
    }



    private Violation mapToEntity(ViolationRequestDTO dto, VehicleInfo vehicleInfo, UserInfo userInfo) {
        return Violation.builder()
                .vehicleInfo(vehicleInfo)
                .userInfo(userInfo)
                .violationType(ViolationType.valueOf(dto.getViolationType().toUpperCase()))
                .fineAmount(dto.getFineAmount())
                .location(dto.getLocation())
                .timestamp(dto.getTimestamp() != null ? dto.getTimestamp() : LocalDateTime.now())
                .build();
    }

    private ViolationResponseDTO mapToDto(Violation violation) {
        return ViolationResponseDTO.builder()
                .vehicleNumber(violation.getVehicleInfo().getVehicleNumber())
                .vehicleType(violation.getVehicleInfo().getVehicleType())
                .userId(violation.getUserInfo().getUserId())
                .userName(violation.getUserInfo().getName())
                .userEmail(violation.getUserInfo().getEmail())
                .userPhone(violation.getUserInfo().getPhoneNumber())
                .violationType(violation.getViolationType().name())
                .fineAmount(violation.getFineAmount())
                .location(violation.getLocation())
                .timestamp(violation.getTimestamp())
                .totalFine(violation.getUserInfo().getTotalFine())
                .build();
    }

    private VehicleInfo mapVehicleDtoToEntity(VehicleInfoDTO dto) {
        return VehicleInfo.builder()
                .vehicleId(dto.getId())
                .vehicleNumber(dto.getRegistrationNumber())
                .vehicleType(dto.getType())
                .userId(dto.getOwnerId())
                .build();
    }

    private UserInfo mapUserDtoToEntity(UserInfoDTO dto) {
        return UserInfo.builder()
                .userId(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .role(com.dibya.programming.Enum.Role.valueOf(dto.getRole()))
                .build();
    }

}
