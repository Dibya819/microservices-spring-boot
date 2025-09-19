package com.dibya.programming.globalexceptionhandling;

import lombok.Getter;

@Getter
public class UserNotFoundByEmailException extends RuntimeException{
    private final String Email;

    public UserNotFoundByEmailException(String Email) {
        super("User Not found with this Email: "+Email);
        this.Email = Email;
    }

}
