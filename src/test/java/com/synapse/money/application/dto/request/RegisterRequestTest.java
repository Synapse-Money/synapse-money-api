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
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Should fail when firstName is blank")
    void shouldFailWhenFirstNameIsBlank() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("First name is required");
    }

    @Test
    @DisplayName("Should fail when firstName is too short")
    void shouldFailWhenFirstNameIsTooShort() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("J")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "First name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when firstName is too long")
    void shouldFailWhenFirstNameIsTooLong() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("a".repeat(51))
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "First name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when lastName is blank")
    void shouldFailWhenLastNameIsBlank() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Last name is required");
    }

    @Test
    @DisplayName("Should fail when lastName is too short")
    void shouldFailWhenLastNameIsTooShort() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("D")
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Last name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when lastName is too long")
    void shouldFailWhenLastNameIsTooLong() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("a".repeat(51))
                .email("john.doe@example.com")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Last name must be between 2 and 50 characters");
    }

    @Test
    @DisplayName("Should fail when email is blank")
    void shouldFailWhenEmailIsBlank() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email is required");
    }

    @Test
    @DisplayName("Should fail when email is invalid")
    void shouldFailWhenEmailIsInvalid() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("invalid-email")
                .password("securePassword123")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Email must be valid");
    }

    @Test
    @DisplayName("Should fail when password is blank")
    void shouldFailWhenPasswordIsBlank() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Password is required");
    }

    @Test
    @DisplayName("Should fail when password is too short")
    void shouldFailWhenPasswordIsTooShort() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("short")
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Password must be between 8 and 100 characters");
    }

    @Test
    @DisplayName("Should fail when password is too long")
    void shouldFailWhenPasswordIsTooLong() {
        RegisterRequest request = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("a".repeat(101))
                .build();

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo(
                "Password must be between 8 and 100 characters");
    }
}