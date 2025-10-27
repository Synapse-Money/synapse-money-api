package com.synapse.money.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("DomainException Tests")
class DomainExceptionTest {

    @Test
    @DisplayName("Should create exception with message")
    void shouldCreateExceptionWithMessage() {
        TestDomainException exception = new TestDomainException("Test error message");

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getCause()).isNull();
    }

    @Test
    @DisplayName("Should create exception with message and cause")
    void shouldCreateExceptionWithMessageAndCause() {
        Throwable           cause     = new RuntimeException("Root cause");
        TestDomainException exception = new TestDomainException("Test error message", cause);

        assertThat(exception).isNotNull();
        assertThat(exception.getMessage()).isEqualTo("Test error message");
        assertThat(exception.getCause()).isEqualTo(cause);
        assertThat(exception.getCause().getMessage()).isEqualTo("Root cause");
    }

    private static class TestDomainException extends DomainException {
        public TestDomainException(String message) {
            super(message);
        }

        public TestDomainException(
                String message,
                Throwable cause) {
            super(message, cause);
        }
    }
}