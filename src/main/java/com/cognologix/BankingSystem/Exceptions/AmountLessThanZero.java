package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AmountLessThanZero extends RuntimeException {
    AmountLessThanZero(){super();}
    public AmountLessThanZero(String message){super(message);}
    @ExceptionHandler(value = AmountLessThanZero.class)
    public ResponseEntity<String> AmountLessThanZero(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
