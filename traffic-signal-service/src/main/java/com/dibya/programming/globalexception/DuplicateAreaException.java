package com.dibya.programming.globalexception;

public class DuplicateAreaException extends RuntimeException {
    public DuplicateAreaException(String area) {
        super("Here already a signal present: "+area);
    }
}
