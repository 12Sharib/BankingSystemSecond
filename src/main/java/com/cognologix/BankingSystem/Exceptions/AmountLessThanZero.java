package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AmountLessThanZero extends RuntimeException {
    public AmountLessThanZero(){
        super();
    }
    public AmountLessThanZero(String message){
        super(message);
    }
    @ExceptionHandler(AmountLessThanZero.class)
    public ResponseEntity<ExceptionResponse> amountLessThanZero(Exception exception){
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);
    }

}
