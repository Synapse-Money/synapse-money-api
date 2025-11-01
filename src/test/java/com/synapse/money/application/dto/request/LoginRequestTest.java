package com.synapse.money.application.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("LoginRequest Tests")
class LoginRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create valid LoginRequest")
    void shouldCreateValidLoginRequest() {
        LoginRequest request = new LoginRequest(
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail when email is blank")
    void shouldFailWhenEmailIsBlank() {
        LoginRequest request = new LoginRequest(
                "",
                "securePassword123"
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email is required");
    }

    @Test
    @DisplayName("Should fail when email is null")
    void shouldFailWhenEmailIsNull() {
        LoginRequest request = new LoginRequest(
                null,
                "securePassword123"
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Email is required"));
    }

    @Test
    @DisplayName("Should fail when email is invalid")
    void shouldFailWhenEmailIsInvalid() {
        LoginRequest request = new LoginRequest(
                "invalid-email",
                "securePassword123"
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
    }

    @Test
    @DisplayName("Should fail when password is blank")
    void shouldFailWhenPasswordIsBlank() {
        LoginRequest request = new LoginRequest(
                "john.doe@example.com",
                ""
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password is required");
    }

    @Test
    @DisplayName("Should fail when password is null")
    void shouldFailWhenPasswordIsNull() {
        LoginRequest request = new LoginRequest(
                "john.doe@example.com",
                null
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password is required");
    }

    @Test
    @DisplayName("Should fail when both email and password are blank")
    void shouldFailWhenBothEmailAndPasswordAreBlank() {
        LoginRequest request = new LoginRequest(
                "",
                ""
        );

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(2);
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Email is required"));
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Password is required"));
    }
}