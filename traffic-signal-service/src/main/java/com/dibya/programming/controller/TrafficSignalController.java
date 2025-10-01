package com.dibya.programming.controller;

import com.dibya.programming.dtos.TrafficSignalRequestDto;
import com.dibya.programming.dtos.TrafficSignalResponseDto;
import com.dibya.programming.dtos.UpdateTrafficSignalRequestDto;
import com.dibya.programming.service.TrafficSignalServiceInterface;
import io.github.resilience4j.core.exception.AcquirePermissionCancelledException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/traffic")
public class TrafficSignalController {

    private final TrafficSignalServiceInterface service;

    @Autowired
    public TrafficSignalController(TrafficSignalServiceInterface service) {
        this.service = service;
    }

    @PostMapping("/traffic-signals")
    public ResponseEntity<TrafficSignalResponseDto> createSignal(@RequestBody @Valid TrafficSignalRequestDto requestDto,
                                                                 @RequestHeader("X-User-Role") String role) {
        if (!"ADMIN".equals(role) && !"TRAFFIC_OFFICER".equals(role)) {
            throw new AccessDeniedException("You can not create a traffic signal !!");
        }
        TrafficSignalResponseDto responseDto = service.createTrafficSignal(requestDto);
        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);

    }

    @GetMapping("/traffic-signals")
    public ResponseEntity<List<TrafficSignalResponseDto>> getSignals(  @RequestParam(name = "city", required = false) String city,
                                                                 @RequestParam(name = "area", required = false) String area,
                                                                 @RequestHeader("X-User-Role") String role){
        if (!"ADMIN".equals(role) && !"TRAFFIC_OFFICER".equals(role)) {
            throw new AccessDeniedException("You are not allowed to view traffic signals");
        }
        List<TrafficSignalResponseDto> signals=service.listTrafficSignals(area,city);
        return new ResponseEntity<>(signals,HttpStatus.OK);
    }

    @PutMapping("/traffic-signals/{area}")
    public ResponseEntity<TrafficSignalResponseDto> updateSignals(@PathVariable String area,
                                                                  @RequestHeader("X-User-Role") String role,
                                                                  @RequestBody @Valid UpdateTrafficSignalRequestDto requestDto){
        if (!"ADMIN".equals(role) && !"TRAFFIC_OFFICER".equals(role)) {
            throw new AccessDeniedException("You are not allowed to update traffic signals");
        }
        TrafficSignalResponseDto response = service.updateTrafficSignal(area, requestDto);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }
}