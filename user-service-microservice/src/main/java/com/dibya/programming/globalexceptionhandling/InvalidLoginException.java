package com.dibya.programming.globalexceptionhandling;

public class InvalidLoginException extends RuntimeException {
    public InvalidLoginException(String email) {
        super("Invalid login credentials for email: " + email);
    }
}
