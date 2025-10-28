package com.synapse.money.infrastructure.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("BCryptPasswordHasher Tests")
class BCryptPasswordHasherTest {

    private BCryptPasswordHasher passwordHasher;

    @BeforeEach
    void setUp() {
        passwordHasher = new BCryptPasswordHasher(new BCryptPasswordEncoder());
    }

    @Test
    @DisplayName("Should hash password successfully")
    void shouldHashPasswordSuccessfully() {
        String rawPassword = "MySecurePassword123";

        String hashedPassword = passwordHasher.hash(rawPassword);

        assertThat(hashedPassword).isNotNull();
        assertThat(hashedPassword).isNotEqualTo(rawPassword);
        assertThat(hashedPassword).startsWith("$2a$");
        assertThat(hashedPassword.length()).isGreaterThan(50);
    }

    @Test
    @DisplayName("Should generate different hashes for same password")
    void shouldGenerateDifferentHashesForSamePassword() {
        String rawPassword = "MySecurePassword123";

        String hash1 = passwordHasher.hash(rawPassword);
        String hash2 = passwordHasher.hash(rawPassword);

        assertThat(hash1).isNotEqualTo(hash2);
    }

    @Test
    @DisplayName("Should match raw password with hashed password")
    void shouldMatchRawPasswordWithHashedPassword() {
        String rawPassword    = "MySecurePassword123";
        String hashedPassword = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches(rawPassword, hashedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("Should not match different passwords")
    void shouldNotMatchDifferentPasswords() {
        String rawPassword    = "MySecurePassword123";
        String wrongPassword  = "WrongPassword456";
        String hashedPassword = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches(wrongPassword, hashedPassword);

        assertThat(matches).isFalse();
    }

    @Test
    @DisplayName("Should not match raw password with another hash")
    void shouldNotMatchRawPasswordWithAnotherHash() {
        String password1 = "Password123";
        String password2 = "Password456";
        String hash1     = passwordHasher.hash(password1);

        boolean matches = passwordHasher.matches(password2, hash1);

        assertThat(matches).isFalse();
    }

    @Test
    @DisplayName("Should throw exception when hashing null password")
    void shouldThrowExceptionWhenHashingNullPassword() {
        assertThatThrownBy(() -> passwordHasher.hash(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Raw password cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when hashing empty password")
    void shouldThrowExceptionWhenHashingEmptyPassword() {
        assertThatThrownBy(() -> passwordHasher.hash(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Raw password cannot be empty");
    }

    @Test
    @DisplayName("Should throw exception when matching with null raw password")
    void shouldThrowExceptionWhenMatchingWithNullRawPassword() {
        String hashedPassword = passwordHasher.hash("Password123");

        assertThatThrownBy(() -> passwordHasher.matches(null, hashedPassword))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Raw password cannot be null");
    }

    @Test
    @DisplayName("Should throw exception when matching with null hashed password")
    void shouldThrowExceptionWhenMatchingWithNullHashedPassword() {
        assertThatThrownBy(() -> passwordHasher.matches("Password123", null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Hashed password cannot be null");
    }

    @Test
    @DisplayName("Should handle special characters in password")
    void shouldHandleSpecialCharactersInPassword() {
        String rawPassword    = "P@ssw0rd!#$%^&*()";
        String hashedPassword = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches(rawPassword, hashedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("Should handle passwords up to 72 bytes")
    void shouldHandlePasswordsUpTo72Bytes() {
        String rawPassword    = "a".repeat(72);
        String hashedPassword = passwordHasher.hash(rawPassword);

        boolean matches = passwordHasher.matches(rawPassword, hashedPassword);

        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("Should throw exception for passwords longer than 72 bytes")
    void shouldThrowExceptionForPasswordsLongerThan72Bytes() {
        String rawPassword = "a".repeat(73);

        assertThatThrownBy(() -> passwordHasher.hash(rawPassword))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Should be case sensitive")
    void shouldBeCaseSensitive() {
        String rawPassword    = "Password123";
        String hashedPassword = passwordHasher.hash(rawPassword);

        boolean matchesLowerCase = passwordHasher.matches("password123", hashedPassword);
        boolean matchesUpperCase = passwordHasher.matches("PASSWORD123", hashedPassword);

        assertThat(matchesLowerCase).isFalse();
        assertThat(matchesUpperCase).isFalse();
    }
}