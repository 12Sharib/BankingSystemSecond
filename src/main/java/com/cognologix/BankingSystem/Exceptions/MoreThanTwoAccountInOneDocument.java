package com.cognologix.BankingSystem.Exceptions;

import com.cognologix.BankingSystem.Response.ExceptionResponse;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.PersistenceException;

@RestControllerAdvice
public class MoreThanTwoAccountInOneDocument extends PersistenceException {
    @ExceptionHandler(IncorrectResultSizeDataAccessException.class)
    public ResponseEntity<ExceptionResponse> moreThanTwoAccountInOneDocument(Exception exception){
       return new ResponseEntity<ExceptionResponse>(new ExceptionResponse(exception.getMessage(),false), HttpStatus.NOT_FOUND);
    }
}
