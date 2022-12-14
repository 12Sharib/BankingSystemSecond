package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidCustomerId extends RuntimeException{
    public InvalidCustomerId(){
        super();
    }
    public InvalidCustomerId(String message){
        super(message);
    }
    @ExceptionHandler(value = InvalidCustomerId.class)
    public ResponseEntity<ExceptionResponse> invalidCustomerId(Exception exception){
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(),false),HttpStatus.BAD_REQUEST);
    }
}
