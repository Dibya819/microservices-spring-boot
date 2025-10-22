package com.dibya.programming.controller;

import com.dibya.programming.dtos.UpdateVehicleDTO;
import com.dibya.programming.dtos.VehicleRequestDto;
import com.dibya.programming.dtos.VehicleResponseDto;
import com.dibya.programming.globalexceptionhandling.AccessDeniedException;
import com.dibya.programming.globalexceptionhandling.IllegalArgumentException;
import com.dibya.programming.globalexceptionhandling.UserNotFoundException;
import com.dibya.programming.service.VehicleService;
import com.dibya.programming.service.VehicleServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vehicles")
public class VehicleController {
    private final VehicleService vehicleService;
    private final VehicleServiceImpl vehicleServiceImpl;

    @Autowired
    public VehicleController(VehicleService vehicleService, VehicleServiceImpl vehicleServiceImpl) {
        this.vehicleService = vehicleService;
        this.vehicleServiceImpl = vehicleServiceImpl;
    }

    @PostMapping("/register")
    public ResponseEntity<VehicleResponseDto> registerVehicle(@RequestBody @Valid VehicleRequestDto requestDto,
                                                              @RequestHeader("X-User-Role") String role){
        if (!role.equals("ADMIN")) {
            throw new AccessDeniedException("Only ADMIN can register vehicles");
        }
        Long ownerId = vehicleServiceImpl.getUserId(requestDto.getEmail(), requestDto.getPhoneNumber()).join();
        if (ownerId == null) {
            throw new UserNotFoundException("User not found with given email or phone number");
        }
        VehicleResponseDto responseDto= vehicleService.createVehicle(requestDto,ownerId);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<VehicleResponseDto> updateVehicle(@PathVariable("id") Long id,
                                                            @RequestBody @Valid UpdateVehicleDTO updateDto,
                                                            @RequestHeader("X-User-Role") String role) {

        if (!"ADMIN".equals(role)) {
            throw new AccessDeniedException("Only ADMIN can update vehicles");
        }
        Long newOwnerId=null;
        if (updateDto.getEmail() != null || updateDto.getPhoneNumber() != null) {
            newOwnerId = vehicleServiceImpl.getUserId(updateDto.getEmail(), updateDto.getPhoneNumber()).join();
            if (newOwnerId == null) {
                throw new UserNotFoundException("User not found with given email or phone number");
            }
        }
        VehicleResponseDto response = vehicleService.updateVehicle(id, updateDto,newOwnerId);
        return new ResponseEntity<>(response,HttpStatus.OK);
    }
   @GetMapping("/uservehicles")
    public ResponseEntity<List<VehicleResponseDto>> getVehicles(@RequestParam(name = "email",required = false)String email,
                                                                @RequestParam(name = "phoneNumber",required = false)String number,
                                                                @RequestHeader("X-User-Role")String role,
                                                                @RequestHeader("X-User-Id") Long currentUserId){
       Long userId;

       if ("ADMIN".equals(role) || "TRAFFIC_OFFICER".equals(role)) {
           if (email == null && number == null) {
               throw new IllegalArgumentException("Please provide email or phone number to fetch user vehicles");
           }
           userId = vehicleServiceImpl.getUserId(email, number).join();
           if (userId == null) {
               throw new UserNotFoundException("User not found with given email/phone");
           }

       } else if ("DRIVER".equals(role)) {
           if (email != null || number != null) {
               userId = vehicleServiceImpl.getUserId(email, number).join();
               if (userId == null) {
                   throw new UserNotFoundException("User not found with given email/phone");
               }
               if (!userId.equals(currentUserId)) {
                   throw new AccessDeniedException("Access denied: You can only view your own vehicles");
               }
           } else {
               userId = currentUserId;
           }

       } else {
           throw new AccessDeniedException("Unauthorized role");
       }

       List<VehicleResponseDto> vehicleResponse = vehicleServiceImpl.getVehiclesByOwner(userId);
       return ResponseEntity.ok(vehicleResponse);
   }
   @DeleteMapping("/{vehicleId}")
   public ResponseEntity<String> deleteVehicle(@PathVariable("vehicleId") Long vehicleId,
                                               @RequestHeader("X-User-Role")String role,
                                               @RequestParam(name = "email",required = false)String email,
                                               @RequestParam(name = "phoneNumber",required = false)String number){
        if(!"ADMIN".equals(role)){
            throw new AccessDeniedException("Only ADMIN can remove vehicles");
        }
        Long ownerId=vehicleServiceImpl.getUserId(email,number).join();
       if (ownerId == null) {
           throw new UserNotFoundException("User not found with given email/phone");
       }
       vehicleService.deleteVehicleOfUser(vehicleId,ownerId);
       return new ResponseEntity<>("Deleted Successfully",HttpStatus.OK);
   }
    @GetMapping("/details/{registrationNumber}")
    public ResponseEntity<VehicleResponseDto> getVehicleDetails(@PathVariable("registrationNumber") String registrationNumber,
                                                                @RequestHeader("X-User-Role") String role,
                                                                @RequestHeader("X-User-Id") Long currentUserId) {
        VehicleResponseDto vehicleResponse = vehicleServiceImpl.getVehicleByRegistrationNumber(registrationNumber);

        if ("DRIVER".equals(role) && !vehicleResponse.getOwnerId().equals(currentUserId)) {
            throw new AccessDeniedException("You are not allowed to view this vehicle");
        }
        return new ResponseEntity<>(vehicleResponse, HttpStatus.OK);
    }

}
