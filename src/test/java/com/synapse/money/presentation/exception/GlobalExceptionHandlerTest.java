package com.synapse.money.presentation.exception;

import com.synapse.money.domain.exception.EmailAlreadyExistsException;
import com.synapse.money.domain.exception.InvalidCredentialsException;
import com.synapse.money.presentation.dto.ErrorResponse;
import com.synapse.money.presentation.dto.ValidationErrorResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private static final String EXISTING_EMAIL = "existing@example.com";
    private static final String INVALID_CREDENTIALS_MESSAGE = "Invalid email or password";
    private static final String GENERIC_ERROR_MESSAGE = "An unexpected error occurred";
    private static final String VALIDATION_FAILED_MESSAGE = "Validation failed";
    private static final String EMAIL_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private static final String EMAIL_ERROR_MESSAGE = "must be a valid email";
    private static final String PASSWORD_ERROR_MESSAGE = "must not be blank";

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Should handle EmailAlreadyExistsException and return 409")
    void shouldHandleEmailAlreadyExistsException() {
        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(EXISTING_EMAIL);

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleEmailAlreadyExists(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.CONFLICT.value());
        assertThat(response.getBody().message()).contains(EXISTING_EMAIL);
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle InvalidCredentialsException and return 401")
    void shouldHandleInvalidCredentialsException() {
        InvalidCredentialsException exception = new InvalidCredentialsException(INVALID_CREDENTIALS_MESSAGE);

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleInvalidCredentials(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
        assertThat(response.getBody().message()).isEqualTo(INVALID_CREDENTIALS_MESSAGE);
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle MethodArgumentNotValidException and return 400 with field errors")
    void shouldHandleMethodArgumentNotValidException() {
        BindingResult                   bindingResult = mock(BindingResult.class);
        MethodArgumentNotValidException exception     = new MethodArgumentNotValidException(null, bindingResult);

        FieldError emailError    = new FieldError("registerRequest", EMAIL_FIELD, EMAIL_ERROR_MESSAGE);
        FieldError passwordError = new FieldError("registerRequest", PASSWORD_FIELD, PASSWORD_ERROR_MESSAGE);

        when(bindingResult.getAllErrors()).thenReturn(List.of(emailError, passwordError));

        ResponseEntity<ValidationErrorResponse> response =
                exceptionHandler.handleValidationErrors(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getBody().message()).isEqualTo(VALIDATION_FAILED_MESSAGE);
        assertThat(response.getBody().errors()).hasSize(2);
        assertThat(response.getBody().errors().get(EMAIL_FIELD)).isEqualTo(EMAIL_ERROR_MESSAGE);
        assertThat(response.getBody().errors().get(PASSWORD_FIELD)).isEqualTo(PASSWORD_ERROR_MESSAGE);
        assertThat(response.getBody().timestamp()).isNotNull();
    }

    @Test
    @DisplayName("Should handle generic Exception and return 500")
    void shouldHandleGenericException() {
        Exception exception = new RuntimeException("Unexpected error");

        ResponseEntity<ErrorResponse> response =
                exceptionHandler.handleGenericException(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().status()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getBody().message()).isEqualTo(GENERIC_ERROR_MESSAGE);
        assertThat(response.getBody().timestamp()).isNotNull();
    }
}