package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Component
@RestControllerAdvice
public class InvalidAccountNumber extends RuntimeException {
    public InvalidAccountNumber(){
        super();
    }
    public InvalidAccountNumber(String message){
        super(message);
    }
    @ExceptionHandler(value = InvalidAccountNumber.class)
    public ResponseEntity<ExceptionResponse> invalidAccountNumber(Exception exception) {
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);
    }
}
