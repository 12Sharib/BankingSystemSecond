package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Enums.Error.ErrorCodes;
import com.cognologix.BankingSystem.Response.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

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
        return new ResponseEntity<>(new ExceptionResponse(ErrorCodes.INVALID_TRANSACTION_ID.getCode(),exception.getMessage(),false, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
