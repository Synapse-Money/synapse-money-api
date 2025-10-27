package com.synapse.money.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserAlreadyExistsException Tests")
class UserAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Should create exception with email in message")
    void shouldCreateExceptionWithEmailInMessage() {
        String email = "john.doe@example.com";

        UserAlreadyExistsException exception = new UserAlreadyExistsException(email);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("User with email 'john.doe@example.com' already exists");
        assertThat(exception).isInstanceOf(DomainException.class);
    }

    @Test
    @DisplayName("Should be a runtime exception")
    void shouldBeRuntimeException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("test@example.com");

        assertThat(exception).isInstanceOf(RuntimeException.class);
    }
}