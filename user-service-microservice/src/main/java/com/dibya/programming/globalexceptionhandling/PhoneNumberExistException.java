package com.dibya.programming.globalexceptionhandling;

public class PhoneNumberExistException extends RuntimeException{

    public PhoneNumberExistException(String number){
        super("Phone number already exists: "+number);
    }
}
