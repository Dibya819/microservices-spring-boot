package com.dibya.programming.globalexceptionhandling;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(Long id) {
        super("Vehicle not found with id: " + id);
    }
    public VehicleNotFoundException(String regNum) {
        super("Vehicle not found with registration number : " + regNum);
    }
}
