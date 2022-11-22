package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotEligibleForCreditCard extends RuntimeException {
    NotEligibleForCreditCard(){super();}
    public NotEligibleForCreditCard(String message) { super(message);}
    @ExceptionHandler(value = NotEligibleForCreditCard.class)
    public ResponseEntity<String> NotEligibleForCreditCard(Exception exception) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);

    }
}
