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

@DisplayName("RegisterRequest Tests")
class RegisterRequestTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Should create valid RegisterRequest")
    void shouldCreateValidRegisterRequest() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail when firstName is blank")
    void shouldFailWhenFirstNameIsBlank() {
        RegisterRequest request = new RegisterRequest(
                "",
                "Doe",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("First name is required"));
    }

    @Test
    @DisplayName("Should fail when firstName is too short")
    void shouldFailWhenFirstNameIsTooShort() {
        RegisterRequest request = new RegisterRequest(
                "J",
                "Doe",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "First name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when firstName is too long")
    void shouldFailWhenFirstNameIsTooLong() {
        RegisterRequest request = new RegisterRequest(
                "a".repeat(51),
                "Doe",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "First name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when lastName is blank")
    void shouldFailWhenLastNameIsBlank() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Last name is required"));
    }

    @Test
    @DisplayName("Should fail when lastName is too short")
    void shouldFailWhenLastNameIsTooShort() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "D",
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Last name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when lastName is too long")
    void shouldFailWhenLastNameIsTooLong() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "a".repeat(51),
                "john.doe@example.com",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Last name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when email is blank")
    void shouldFailWhenEmailIsBlank() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email is required");
    }

    @Test
    @DisplayName("Should fail when email is invalid")
    void shouldFailWhenEmailIsInvalid() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "invalid-email",
                "securePassword123"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
    }

    @Test
    @DisplayName("Should fail when password is blank")
    void shouldFailWhenPasswordIsBlank() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                ""
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getMessage().equals("Password is required"));
    }

    @Test
    @DisplayName("Should fail when password is too short")
    void shouldFailWhenPasswordIsTooShort() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "short"
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Password must be between 8 and 100 characters");
    }

    @Test
    @DisplayName("Should fail when password is too long")
    void shouldFailWhenPasswordIsTooLong() {
        RegisterRequest request = new RegisterRequest(
                "John",
                "Doe",
                "john.doe@example.com",
                "a".repeat(101)
        );

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Password must be between 8 and 100 characters");
    }
}