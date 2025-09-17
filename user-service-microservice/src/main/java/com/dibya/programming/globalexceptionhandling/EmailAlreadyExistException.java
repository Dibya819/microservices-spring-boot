package com.dibya.programming.globalexceptionhandling;

public class EmailAlreadyExistException extends RuntimeException{

    public EmailAlreadyExistException(String email){
        super("Email already exists: "+email);
    }
}
