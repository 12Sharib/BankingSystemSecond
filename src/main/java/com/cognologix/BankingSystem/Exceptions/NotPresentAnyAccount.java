package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class NotPresentAnyAccount extends RuntimeException {
    public NotPresentAnyAccount(){
        super();
    }
    public NotPresentAnyAccount(String message){
        super(message);
    }
    @ExceptionHandler(value = NotPresentAnyAccount.class)
    public ResponseEntity<ExceptionResponse> notPresentAnyAccount(Exception exception) {
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.BAD_REQUEST);
    }

}
