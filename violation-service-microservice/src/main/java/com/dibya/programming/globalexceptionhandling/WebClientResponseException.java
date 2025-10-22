package com.dibya.programming.globalexceptionhandling;

public class WebClientResponseException extends RuntimeException {
    public WebClientResponseException(String message) {
        super(message);
    }
}
