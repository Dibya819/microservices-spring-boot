package com.dibya.programming.globalexception;

public class TrafficSignalNotFoundException extends RuntimeException {
    public TrafficSignalNotFoundException(String message) {
        super(message);
    }
}
