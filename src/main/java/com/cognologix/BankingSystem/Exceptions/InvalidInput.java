package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Enums.Error.ErrorCodes;
import com.cognologix.BankingSystem.Response.ExceptionResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class InvalidInput {
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> invalidInput(){
        log.fatal("Invalid Input, Closed at starting level");
        return new ResponseEntity<>(new ExceptionResponse(ErrorCodes.INVALID_INPUT.getCode(), "Invalid Input, Provide Valid Input",false, LocalDateTime.now()),HttpStatus.BAD_REQUEST);
    }
}
