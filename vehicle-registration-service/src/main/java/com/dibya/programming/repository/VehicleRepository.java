package com.dibya.programming.repository;

import com.dibya.programming.model.Vehicles;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface VehicleRepository extends JpaRepository<Vehicles,Long> {

    boolean existsByRegistrationNumber(@Size(min = 6, max = 15,
            message = "Registration number must be between 6 and 15 characters")
                                       @NotBlank(message = "Registration number is required") String registrationNumber);
    List<Vehicles> findByOwnerId(Long ownerId);
}
