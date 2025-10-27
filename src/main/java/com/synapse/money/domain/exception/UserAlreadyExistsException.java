package com.synapse.money.domain.exception;

public class UserAlreadyExistsException extends DomainException {

    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists");
    }
}