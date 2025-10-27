package com.synapse.money.domain.exception;

public class InvalidCredentialsException extends DomainException {

    public InvalidCredentialsException() {
        super("Invalid email or password");
    }
}