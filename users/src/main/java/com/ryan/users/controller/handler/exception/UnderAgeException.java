package com.ryan.users.controller.handler.exception;

public class UnderAgeException extends RuntimeException {
    public UnderAgeException(String message) {
        super(message);
    }
}
