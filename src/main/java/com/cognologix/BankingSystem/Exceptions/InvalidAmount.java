package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Enums.Error.ErrorCodes;
import com.cognologix.BankingSystem.Response.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class InvalidAmount extends RuntimeException{
    public InvalidAmount(){
        super();
    }
    public InvalidAmount(String message){
        super(message);
    }
    @ExceptionHandler(InvalidAmount.class)
    public ResponseEntity<ExceptionResponse> invalidAmounts(Exception exception){
        log.throwing(Level.ERROR,exception);
        return new ResponseEntity<>(new ExceptionResponse(ErrorCodes.INVALID_AMOUNT.getCode(), exception.getMessage(),false, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }

}
