package com.dibya.programming.repository;

import com.dibya.programming.model.VehicleInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleInfoRepository extends JpaRepository<VehicleInfo, Long> {
    Optional<VehicleInfo> findByVehicleNumber(String vehicleNumber);

    Optional<VehicleInfo> findVehicleByVehicleId(Long id);


}
