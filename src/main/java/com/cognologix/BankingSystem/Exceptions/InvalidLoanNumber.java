package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Enums.Error.ErrorCodes;
import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class InvalidLoanNumber extends RuntimeException{
    InvalidLoanNumber(){
        super();
    }
    public InvalidLoanNumber(String message){
        super(message);
    }
    @ExceptionHandler(InvalidLoanNumber.class)
    public ResponseEntity<ExceptionResponse> invalidLoanNumber(Exception exception){
        return new ResponseEntity<>(new ExceptionResponse(ErrorCodes.INVALID_LOAN_NUMBER.getCode(), exception.getMessage(), false, LocalDateTime.now()), HttpStatus.BAD_REQUEST);
    }
}
