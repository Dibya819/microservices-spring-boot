package com.dibya.programming.service;

import com.dibya.programming.dtos.TrafficSignalRequestDto;
import com.dibya.programming.dtos.TrafficSignalResponseDto;
import com.dibya.programming.dtos.UpdateTrafficSignalRequestDto;

import java.util.List;

public interface TrafficSignalServiceInterface {
    TrafficSignalResponseDto createTrafficSignal(TrafficSignalRequestDto requestDto);
    List<TrafficSignalResponseDto> listTrafficSignals(String area,String city);
    TrafficSignalResponseDto updateTrafficSignal(String area, UpdateTrafficSignalRequestDto dto);


}
