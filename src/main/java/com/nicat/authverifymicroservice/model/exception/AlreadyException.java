package com.nicat.authverifymicroservice.model.exception;

public class AlreadyException extends RuntimeException {
    public AlreadyException(String message) {
        super(message);
    }
}