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
public class InsufficientBalance extends RuntimeException {
    public InsufficientBalance(){
        super();
    }
    public InsufficientBalance(String message){
        super(message);
    }
    @ExceptionHandler(InsufficientBalance.class)
    public ResponseEntity<ExceptionResponse> amountLessThanZero(Exception exception){
        log.throwing(Level.ERROR,exception);
        return new ResponseEntity<>(new ExceptionResponse(ErrorCodes.INSUFFICIENT_BALANCE.getCode(), exception.getMessage(),false, LocalDateTime.now().minusNanos(500000)), HttpStatus.BAD_REQUEST);
    }

}
