package com.dibya.programming.repository;

import com.dibya.programming.model.TrafficSignal;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignalServiceRepo extends JpaRepository<TrafficSignal,Long> {


    boolean existsByLocationArea(String area);
    List<TrafficSignal> findByLocationAreaAndLocationCity(String area, String city);
    List<TrafficSignal> findByLocationCity(String city);
    TrafficSignal findByLocationArea(String normalizedArea);
}
