package com.synapse.money.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("User Domain Entity Tests")
class UserTest {

    @Test
    @DisplayName("Should create user with valid data")
    void shouldCreateUserWithValidData() {
        User user = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(1L);
        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(user.getPassword()).isEqualTo("hashedPassword123");
        assertThat(user.getFirstName()).isEqualTo("John");
        assertThat(user.getLastName()).isEqualTo("Doe");
        assertThat(user.getCreatedAt()).isNotNull();
        assertThat(user.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should get full name correctly")
    void shouldGetFullNameCorrectly() {
        User user = User.builder()
                .firstName("John")
                .lastName("Doe")
                .build();

        String fullName = user.getFullName();

        assertThat(fullName).isEqualTo("John Doe");
    }

    @Test
    @DisplayName("Should normalize email to lowercase")
    void shouldNormalizeEmailToLowercase() {
        User user = User.builder()
                .email("John.Doe@EXAMPLE.COM")
                .build();

        String normalizedEmail = user.getNormalizedEmail();

        assertThat(normalizedEmail).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return null when normalizing null email")
    void shouldReturnNullWhenNormalizingNullEmail() {
        User user = User.builder().build();

        String normalizedEmail = user.getNormalizedEmail();

        assertThat(normalizedEmail).isNull();
    }

    @Test
    @DisplayName("Should validate email format")
    void shouldValidateEmailFormat() {
        assertThat(User.isValidEmail("test@example.com")).isTrue();
        assertThat(User.isValidEmail("user.name@domain.co.uk")).isTrue();
        assertThat(User.isValidEmail("user+tag@example.com")).isTrue();

        assertThat(User.isValidEmail("invalid")).isFalse();
        assertThat(User.isValidEmail("@example.com")).isFalse();
        assertThat(User.isValidEmail("user@")).isFalse();
    }

    @Test
    @DisplayName("Should validate password strength")
    void shouldValidatePasswordStrength() {
        assertThat(User.isValidPassword("Password123")).isTrue();
        assertThat(User.isValidPassword("MyP@ssw0rd")).isTrue();

        assertThat(User.isValidPassword("short1A")).isFalse();
        assertThat(User.isValidPassword("alllowercase123")).isFalse();
        assertThat(User.isValidPassword("ALLUPPERCASE123")).isFalse();
        assertThat(User.isValidPassword("NoDigitsHere")).isFalse();
    }

    @Test
    @DisplayName("Should compare users by email case insensitive")
    void shouldCompareUsersByEmailCaseInsensitive() {
        User user1 = User.builder().email("john@example.com").build();
        User user2 = User.builder().email("JOHN@EXAMPLE.COM").build();
        User user3 = User.builder().email("jane@example.com").build();

        assertThat(user1.isSameEmail(user2)).isTrue();
        assertThat(user1.isSameEmail(user3)).isFalse();
    }
}