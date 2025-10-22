package com.dibya.programming.repository;

import com.dibya.programming.Enum.ViolationType;
import com.dibya.programming.model.UserInfo;
import com.dibya.programming.model.VehicleInfo;
import com.dibya.programming.model.Violation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ViolationRepository extends JpaRepository<Violation, Long> {

    List<Violation> findByUserInfoAndVehicleInfo(UserInfo user, VehicleInfo vehicle);

    List<Violation> findByUserInfo(UserInfo user);

    List<Violation> findByVehicleInfo(VehicleInfo vehicle);

    Optional<Violation> findByUserInfoAndVehicleInfoAndViolationTypeAndTimestamp(UserInfo user, VehicleInfo vehicle, ViolationType violationType, LocalDateTime violationTime);
}
