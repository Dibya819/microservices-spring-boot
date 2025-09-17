package com.dibya.programming.globalexceptionhandling;

import com.dibya.programming.dtos.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandle {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex, HttpServletRequest request) {
        String uri=request.getRequestURI();
        ErrorResponse errorResponse=new ErrorResponse(LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "User Not found!!!",ex.getMessage(),uri);
        return new ResponseEntity<>(errorResponse,HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse>handleValidationError(MethodArgumentNotValidException ex,HttpServletRequest request){
        List<String>error=ex.getBindingResult()
                            .getFieldErrors()
                            .stream()
                            .map(err->err.getField()+": "+err.getDefaultMessage())
                            .toList();
        ErrorResponse errorResponse=new ErrorResponse(LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Invalid Input!!",error,request.getRequestURI());
        return new ResponseEntity<>(errorResponse,HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(DuplicateUserException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUserFoundException(DuplicateUserException ex, HttpServletRequest request) {
        String uri=request.getRequestURI();
        ErrorResponse errorResponse=new ErrorResponse(LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Duplicate User Data!!!",ex.getErrors(),uri);
        return new ResponseEntity<>(errorResponse,HttpStatus.CONFLICT);
    }

}
