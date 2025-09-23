package com.dibya.programming.globalexceptionhandling;

public class DuplicateVehicleException extends RuntimeException {
    public DuplicateVehicleException(String registrationNumber) {
        super("Vehicle with registration number already exists: " + registrationNumber);
    }
}
