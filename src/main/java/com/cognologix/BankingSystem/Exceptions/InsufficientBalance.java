package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InsufficientBalance extends RuntimeException {
    public InsufficientBalance(){
        super();
    }
    public InsufficientBalance(String message){
        super(message);
    }
    @ExceptionHandler(InsufficientBalance.class)
    public ResponseEntity<ExceptionResponse> amountLessThanZero(Exception exception){
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage()+"33333",false), HttpStatus.BAD_REQUEST);
    }

}
