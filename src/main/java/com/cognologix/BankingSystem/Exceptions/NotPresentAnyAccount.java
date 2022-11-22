package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotPresentAnyAccount extends RuntimeException {
    NotPresentAnyAccount(){super();}
    public NotPresentAnyAccount(String message){
        super(message);
    }
    @ExceptionHandler(value = NotPresentAnyAccount.class)
    public ResponseEntity<String> NotPresentAnyAccount(Exception ex) {
        return new ResponseEntity<>(ex.getMessage() , HttpStatus.BAD_REQUEST);
    }

}
