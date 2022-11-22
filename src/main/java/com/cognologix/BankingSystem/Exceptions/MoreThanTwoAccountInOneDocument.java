package com.cognologix.BankingSystem.Exceptions;

import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;

@RestControllerAdvice
public class MoreThanTwoAccountInOneDocument extends PersistenceException {
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<String> MoreThanTwoAccountInOneDocument(Exception exception){
        return new ResponseEntity<>("Current and Savings are both present on this document, provide valid document ", HttpStatus.BAD_REQUEST);
    }
}
