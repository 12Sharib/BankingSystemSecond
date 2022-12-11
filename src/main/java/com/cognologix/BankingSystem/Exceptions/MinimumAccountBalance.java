package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ExceptionResponse> minimumAccountBalance(Exception exception){
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);

    }
}
