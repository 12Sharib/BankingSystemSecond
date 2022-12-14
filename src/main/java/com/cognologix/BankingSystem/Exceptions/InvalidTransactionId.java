package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Component
@RestControllerAdvice
public class InvalidTransactionId extends RuntimeException{
    public InvalidTransactionId(){
        super();
    }
    public InvalidTransactionId(String message){
        super(message);
    }
    @ExceptionHandler(value = InvalidTransactionId.class)
    public ResponseEntity<ExceptionResponse> invalidTransactionId(Exception exception){
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);
    }
}
