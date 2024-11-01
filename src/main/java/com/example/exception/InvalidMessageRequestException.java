package com.example.exception;

public class InvalidMessageRequestException extends RuntimeException {
    public InvalidMessageRequestException(String message) {
        super(message);
    }
}
