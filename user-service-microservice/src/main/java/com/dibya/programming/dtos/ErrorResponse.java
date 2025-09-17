package com.dibya.programming.dtos;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    private LocalDateTime timeStamp;
    private int status;
    private String error;
    private String message;
    private List<String> messages;
    private String path;

    public ErrorResponse(LocalDateTime timeStamp, int status, String error,List<String> messages, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.messages = messages;
        this.path = path;
    }
    public ErrorResponse(LocalDateTime timeStamp, int status, String error, String message, String path) {
        this.timeStamp = timeStamp;
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

}
