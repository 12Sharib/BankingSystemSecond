package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidAmount extends RuntimeException{
    public InvalidAmount(){
        super();
    }
    public InvalidAmount(String message){
        super(message);
    }
    @ExceptionHandler(InvalidAmount.class)
    public ResponseEntity<ExceptionResponse> invalidAmounts(Exception exception){
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);
    }

}
