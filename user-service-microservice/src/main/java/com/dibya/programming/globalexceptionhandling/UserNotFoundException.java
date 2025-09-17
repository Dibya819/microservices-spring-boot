package com.dibya.programming.globalexceptionhandling;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException{
private final long id;
    public UserNotFoundException(long id) {
    super("User not found with this id: "+id);
    this.id=id;
    }

}
