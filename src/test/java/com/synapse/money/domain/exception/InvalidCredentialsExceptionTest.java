package com.synapse.money.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("InvalidCredentialsException Tests")
class InvalidCredentialsExceptionTest {

    @Test
    @DisplayName("Should create exception with default message")
    void shouldCreateExceptionWithDefaultMessage() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Invalid credentials");
        assertThat(exception).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Should be a runtime exception")
    void shouldBeRuntimeException() {
        InvalidCredentialsException exception = new InvalidCredentialsException("Invalid credentials");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}