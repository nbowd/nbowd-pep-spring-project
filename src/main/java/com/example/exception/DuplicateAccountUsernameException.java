package com.example.exception;

public class DuplicateAccountUsernameException extends RuntimeException {

    public DuplicateAccountUsernameException(String message) {
        super(message);
    }
    
}
