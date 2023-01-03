package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.ErrorCodes.Codes;
import com.cognologix.BankingSystem.Response.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Component
@RestControllerAdvice
@Log4j2
public class InvalidAccountNumber extends RuntimeException {
    public InvalidAccountNumber(){
        super();
    }
    public InvalidAccountNumber(String message){
        super(message);
    }
    @ExceptionHandler(value = InvalidAccountNumber.class)
    public ResponseEntity<ExceptionResponse> invalidAccountNumber(Exception exception) {
        log.error("*********************",exception);
     //   log.throwing(Level.ERROR,exception);
        return new ResponseEntity<>(new ExceptionResponse(exception.getMessage() +" -:- "+ Codes.INVALID_ACCOUNT_NUMBER.getCode(),false), HttpStatus.BAD_REQUEST);
    }
}
