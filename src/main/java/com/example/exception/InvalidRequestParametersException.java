package com.example.exception;

public class InvalidRequestParametersException extends RuntimeException {
    
    public InvalidRequestParametersException (String message) {
        super(message);
    }
}
