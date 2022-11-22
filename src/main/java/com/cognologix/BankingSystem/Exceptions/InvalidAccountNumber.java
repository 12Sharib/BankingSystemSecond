package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Component
@RestControllerAdvice
public class InvalidAccountNumber extends RuntimeException {
    InvalidAccountNumber(){super();}
    public InvalidAccountNumber(String message){super(message);}

    @ExceptionHandler(value = InvalidAccountNumber.class)
    public ResponseEntity<String> InvalidAccountNumber(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
