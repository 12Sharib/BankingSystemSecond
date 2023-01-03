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
public class InvalidTransactionId extends RuntimeException{
    public InvalidTransactionId(){
        super();
    }
    public InvalidTransactionId(String message){
        super(message);
    }
    @ExceptionHandler(value = InvalidTransactionId.class)
    public ResponseEntity<ExceptionResponse> invalidTransactionId(Exception exception){
        log.throwing(Level.ERROR,exception);
        return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage()+": "+ Codes.INVALID_TRANSACTION_ID.getCode(),false), HttpStatus.BAD_REQUEST);
    }
}
