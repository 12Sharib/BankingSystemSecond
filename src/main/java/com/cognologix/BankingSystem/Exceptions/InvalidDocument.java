package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidDocument extends RuntimeException {
    InvalidDocument(){super();}
    public InvalidDocument(String messaage){super(messaage);}
    @ExceptionHandler(value = InvalidDocument.class)
    public ResponseEntity<String> InvalidDocument(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
