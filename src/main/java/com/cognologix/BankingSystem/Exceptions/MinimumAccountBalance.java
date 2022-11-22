package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MinimumAccountBalance extends RuntimeException {
    public MinimumAccountBalance() {
        super();
    }
    public MinimumAccountBalance(String message) {
        super(message);
    }
    @ExceptionHandler(value=MinimumAccountBalance.class)
    public ResponseEntity<String> MinimumAccountBalance(Exception ex){
        return new ResponseEntity<>("Exception : "+ex.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
