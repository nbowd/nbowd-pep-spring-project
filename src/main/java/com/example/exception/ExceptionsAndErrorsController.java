package com.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionsAndErrorsController {
    @ExceptionHandler(InvalidRequestParametersException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidRequest(InvalidRequestParametersException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(DuplicateAccountUsernameException.class)
    @ResponseStatus(HttpStatus.CONFLICT) 
    public String handleDuplicateUsername(DuplicateAccountUsernameException ex ) {
        return ex.getMessage();
    }
}

