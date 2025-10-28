package com.synapse.money.domain.exception;

public class EmailAlreadyExistsException extends DomainException {

    public EmailAlreadyExistsException(String message) {
        super(message);
    }
}