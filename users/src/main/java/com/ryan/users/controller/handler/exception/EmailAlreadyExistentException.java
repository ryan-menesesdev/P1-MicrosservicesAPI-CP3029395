package com.ryan.users.controller.handler.exception;

public class EmailAlreadyExistentException extends RuntimeException {
    public EmailAlreadyExistentException(String email) {
        super("O seguinte email já foi cadastrado previamente: " + email);
    }
}
