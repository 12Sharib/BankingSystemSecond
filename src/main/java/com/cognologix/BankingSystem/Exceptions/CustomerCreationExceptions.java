package com.cognologix.BankingSystem.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomerCreationExceptions {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> HandleMethodArgumentNotValidException(MethodArgumentNotValidException exception){
        Map<String,String> errors = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(
                (objectError) -> {
                   errors.put(((FieldError)objectError).getField(),objectError.getDefaultMessage());
                });
        return new ResponseEntity<Map<String,String>>(errors, HttpStatus.BAD_REQUEST);
    }
}
