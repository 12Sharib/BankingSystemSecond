package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class InvalidDocument extends RuntimeException {
    public InvalidDocument(){
        super();
    }
    public InvalidDocument(String messaage){
        super(messaage);
    }
    @ExceptionHandler(value = InvalidDocument.class)
    public ResponseEntity<ExceptionResponse> invalidDocument(Exception exception) {
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.NOT_FOUND);

    }
}
