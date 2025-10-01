package com.dibya.programming.service;

import com.dibya.programming.dtos.TrafficSignalRequestDto;
import com.dibya.programming.dtos.TrafficSignalResponseDto;
import com.dibya.programming.dtos.UpdateTrafficSignalRequestDto;
import com.dibya.programming.globalexception.DuplicateAreaException;
import com.dibya.programming.globalexception.TrafficSignalNotFoundException;
import com.dibya.programming.model.Location;
import com.dibya.programming.model.TrafficSignal;
import com.dibya.programming.repository.SignalServiceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TrafficSignalService implements TrafficSignalServiceInterface {

    private final SignalServiceRepo serviceRepo;

    @Autowired
    public TrafficSignalService(SignalServiceRepo serviceRepo) {
        this.serviceRepo = serviceRepo;
    }

    @Override
    public TrafficSignalResponseDto createTrafficSignal(TrafficSignalRequestDto requestDto) {
        if(serviceRepo.existsByLocationArea(requestDto.getArea())){
            throw new DuplicateAreaException(requestDto.getArea());
        }
        TrafficSignal signal=mapToEntity(requestDto);
        TrafficSignal saved=serviceRepo.save(signal);
        return mapToDTO(saved);
    }

    @Override
    public List<TrafficSignalResponseDto> listTrafficSignals(String area, String city) {
        String normalizedCity = city != null ? city.trim().toLowerCase() : null;
        String normalizedArea = area != null ? area.trim().toLowerCase() : null;

        List<TrafficSignal> signal;
        if(normalizedArea!=null && normalizedCity!=null){
            signal=serviceRepo.findByLocationAreaAndLocationCity(area,city);
            if (signal.isEmpty()) {
                throw new TrafficSignalNotFoundException(
                        "No traffic signals found for area '" + area + "' and city '" + city + "'");
            }
        }
        else if (normalizedCity!=null){
            signal=serviceRepo.findByLocationCity(city);
            if (signal.isEmpty()) {
                throw new TrafficSignalNotFoundException(
                        "No traffic signals found for city '" + city + "'");
            }
        }else {
            signal=serviceRepo.findAll();
            if (signal.isEmpty()) {
                throw new TrafficSignalNotFoundException("No traffic signals found");
            }
        }
        return signal.stream().map(this::mapToDTO).toList();
    }

    @Override
    public TrafficSignalResponseDto updateTrafficSignal(String area, UpdateTrafficSignalRequestDto dto) {
        String normalizedArea = area.trim().toLowerCase();
        TrafficSignal signal = serviceRepo.findByLocationArea(area);
        boolean statusChanged = false;
        if (signal == null) {
            throw new TrafficSignalNotFoundException("No traffic signal found for area: " + area);
        }
        mergeUpdateDtoToEntity(dto, signal);
        TrafficSignal updatedSignal=serviceRepo.save(signal);
        return mapToDTO(updatedSignal);

    }

    private TrafficSignal mapToEntity(TrafficSignalRequestDto dto) {
        TrafficSignal.SignalStatus status = TrafficSignal.SignalStatus.RED; // default
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            status = TrafficSignal.SignalStatus.valueOf(dto.getStatus().toUpperCase());
        }

        Location location = Location.builder()
                .city(dto.getCity())
                .state(dto.getState())
                .area(dto.getArea())
                .pincode(dto.getPincode())
                .build();

        return TrafficSignal.builder()
                .location(location)
                .status(status)
                .metadata(dto.getMetadata())
                .manualOverride(false)
                .build();
    }
    private TrafficSignalResponseDto mapToDTO(TrafficSignal trafficSignal){
        return TrafficSignalResponseDto.builder()
                .id(trafficSignal.getId())
                .city(trafficSignal.getLocation().getCity())
                .state(trafficSignal.getLocation().getState())
                .area(trafficSignal.getLocation().getArea())
                .pincode(trafficSignal.getLocation().getPincode())
                .status(trafficSignal.getStatus().name())
                .metadata(trafficSignal.getMetadata())
                .manualOverride(trafficSignal.getManualOverride())
                .lastUpdated(trafficSignal.getLastUpdated())
                .createdAt(trafficSignal.getCreatedAt())
                .updatedAt(trafficSignal.getUpdatedAt())
                .build();
    }
    private void mergeUpdateDtoToEntity(UpdateTrafficSignalRequestDto dto, TrafficSignal signal) {
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            TrafficSignal.SignalStatus newStatus = TrafficSignal.SignalStatus.valueOf(dto.getStatus().toUpperCase());
            if (!signal.getStatus().equals(newStatus)) {
                signal.setStatus(newStatus);
                signal.setManualOverride(true);
            }
        }
    }

}
