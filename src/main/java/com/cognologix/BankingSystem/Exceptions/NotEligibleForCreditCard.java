package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotEligibleForCreditCard extends RuntimeException {
    public NotEligibleForCreditCard(){
        super();
    }
    public NotEligibleForCreditCard(String message) {
        super(message);
    }
    @ExceptionHandler(value = NotEligibleForCreditCard.class)
    public ResponseEntity<ExceptionResponse> notEligibleForCreditCard(Exception exception) {
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);

    }
}
