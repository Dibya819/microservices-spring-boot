package com.dibya.programming.controller;


import com.dibya.programming.dtos.ViolationRequestDTO;
import com.dibya.programming.dtos.ViolationResponseDTO;
import com.dibya.programming.dtos.ViolationUpdateRequestDTO;
import com.dibya.programming.globalexceptionhandling.AccessDeniedException;
import com.dibya.programming.globalexceptionhandling.IllegalArgumentException;
import com.dibya.programming.service.ViolationServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/violations")
public class ViolationController {

    private final ViolationServiceImpl violationService;

    @Autowired
    public ViolationController(ViolationServiceImpl violationService) {
        this.violationService = violationService;
    }

    @PostMapping("/addFine")
    public ResponseEntity<ViolationResponseDTO> addViolation(@RequestHeader("X-User-Id") Long officerId,
                                                             @RequestHeader("X-User-Role") String role,
                                                             @RequestHeader("Authorization") String authHeader,
                                                             @Valid @RequestBody ViolationRequestDTO requestDTO){
        if(!"TRAFFIC_OFFICER".equals(role) && !"ADMIN".equals(role)){
            throw new AccessDeniedException("You are not authorized to add a violation!");
        }

      ViolationResponseDTO responseDTO=violationService.addViolation(officerId,requestDTO,authHeader).join();
        return new ResponseEntity<>(responseDTO,HttpStatus.CREATED);
    }
    @GetMapping("/driver")
    public ResponseEntity<List<ViolationResponseDTO>> getViolationsByDriver(
            @RequestHeader("X-User-Id") Long userId,
            @RequestHeader("X-User-Role") String role,
            @RequestParam(name="email",required = false) String email,
            @RequestParam(name="phoneNumber",required = false) String phoneNumber,
            @RequestParam(name="vehicleNumber",required = false)String vehicleNumber) {

        if ((email == null || email.isBlank()) && (phoneNumber == null || phoneNumber.isBlank()) && (vehicleNumber==null || vehicleNumber.isBlank())) {
            throw new IllegalArgumentException("Either Email,Phone or vehicle Number must be provided!!");
        }

        if (!"DRIVER".equals(role) && !"TRAFFIC_OFFICER".equals(role) && !"ADMIN".equals(role)) {
            throw new AccessDeniedException("You are not authorized to view violations!");
        }

        List<ViolationResponseDTO> violations = violationService.getViolationsByDriver(email, phoneNumber,vehicleNumber);

        if("DRIVER".equals(role)){
            Long violationUserId = violations.isEmpty() ? null : violations.get(0).getUserId();
            if (violationUserId == null || !violationUserId.equals(userId)) {
                throw new AccessDeniedException("You are not authorized to view other driver's violations!");
            }
        }
        return new ResponseEntity<>(violations,HttpStatus.OK);
    }

    @PatchMapping("/update")
    public ResponseEntity<ViolationResponseDTO> updateViolation(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(name="email",required = false) String email,
            @RequestParam(name="phoneNumber",required = false) String phoneNumber,
            @RequestParam(name="vehicleNumber") String vehicleNumber,
            @RequestParam(name="violationType") String violationType,
            @RequestParam(name="timestamp",required = false) String timestamp,
            @Valid @RequestBody ViolationUpdateRequestDTO requestDTO
    ) {
        if (!"TRAFFIC_OFFICER".equals(role) && !"ADMIN".equals(role)) {
            throw new AccessDeniedException("You are not authorized to update a violation!");
        }
        ViolationResponseDTO updatedViolation = violationService.updateViolation(
                email, phoneNumber, vehicleNumber, violationType, timestamp, requestDTO
        );

        return new ResponseEntity<>(updatedViolation, HttpStatus.OK);

    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteViolation(
            @RequestHeader("X-User-Role") String role,
            @RequestParam(name="email",required = false) String email,
            @RequestParam(name="phoneNumber",required = false) String phoneNumber,
            @RequestParam(name="vehicleNumber") String vehicleNumber,
            @RequestParam(name="violationType") String violationType,
            @RequestParam(name="timestamp",required = false) String timestamp
    ) {
        if (!"TRAFFIC_OFFICER".equals(role) && !"ADMIN".equals(role)) {
            throw new AccessDeniedException("You are not authorized to delete a violation!");
        }

        violationService.deleteViolation(email, phoneNumber, vehicleNumber, violationType, timestamp);
        return new ResponseEntity<>("Violation deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/total-fine")
    public ResponseEntity<Double> getTotalFine(@RequestHeader("X-User-Id") Long userId) {
        double totalFine = violationService.getTotalFineForUser(userId);
        return ResponseEntity.ok(totalFine);
    }

    @PostMapping("/clear-fines")
    public ResponseEntity<String> clearFines(@RequestHeader("X-User-Id") Long userId) {
        violationService.clearUserFines(userId);
        return ResponseEntity.ok("All fines cleared successfully.");
    }



}
