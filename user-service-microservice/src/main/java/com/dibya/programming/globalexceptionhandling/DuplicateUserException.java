package com.dibya.programming.globalexceptionhandling;

import lombok.Getter;

import java.util.List;

@Getter
public class DuplicateUserException extends RuntimeException{
    private List<String> errors;
    public DuplicateUserException(List<String>errors){
        super("Duplicate user data found");
        this.errors=errors;
    }
}
