package com.dibya.programming.globalexceptionhandling;

public class UserNotFoundByPhoneNumberException extends RuntimeException{
    private final String number;

    public UserNotFoundByPhoneNumberException(String number) {
        super("User Not found with this number: "+number);
        this.number = number;
    }
}
