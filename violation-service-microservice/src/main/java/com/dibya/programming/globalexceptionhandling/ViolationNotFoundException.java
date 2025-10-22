package com.dibya.programming.globalexceptionhandling;

public class ViolationNotFoundException extends RuntimeException {
    public ViolationNotFoundException(String message) {
        super(message);
    }
}
