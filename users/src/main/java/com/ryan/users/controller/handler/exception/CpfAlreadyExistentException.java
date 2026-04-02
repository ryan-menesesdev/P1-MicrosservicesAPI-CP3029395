package com.ryan.users.controller.handler.exception;

public class CpfAlreadyExistentException extends RuntimeException {
    public CpfAlreadyExistentException(String cpf) {
        super("O seguinte cpf já foi cadastrado previamente: " + cpf);
    }
}
