package com.synapse.money.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AuthResponse Tests")
class AuthResponseTest {

    @Test
    @DisplayName("Should create valid AuthResponse")
    void shouldCreateValidAuthResponse() {
        String token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token(token)
                .user(userResponse)
                .build();

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getToken()).isEqualTo(token);
        assertThat(authResponse.getUser()).isEqualTo(userResponse);
    }

    @Test
    @DisplayName("Should contain user information")
    void shouldContainUserInformation() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token("token123")
                .user(userResponse)
                .build();

        assertThat(authResponse.getUser()).isNotNull();
        assertThat(authResponse.getUser().getId()).isEqualTo(1L);
        assertThat(authResponse.getUser().getFirstName()).isEqualTo("John");
        assertThat(authResponse.getUser().getLastName()).isEqualTo("Doe");
        assertThat(authResponse.getUser().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should contain JWT token")
    void shouldContainJwtToken() {
        String expectedToken =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0" +
                        ".dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token(expectedToken)
                .user(userResponse)
                .build();

        assertThat(authResponse.getToken()).isNotNull();
        assertThat(authResponse.getToken()).isEqualTo(expectedToken);
        assertThat(authResponse.getToken()).startsWith("eyJ");
    }

    @Test
    @DisplayName("Should handle null token")
    void shouldHandleNullToken() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse authResponse = AuthResponse.builder()
                .token(null)
                .user(userResponse)
                .build();

        assertThat(authResponse.getToken()).isNull();
        assertThat(authResponse.getUser()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null user")
    void shouldHandleNullUser() {
        AuthResponse authResponse = AuthResponse.builder()
                .token("token123")
                .user(null)
                .build();

        assertThat(authResponse.getToken()).isEqualTo("token123");
        assertThat(authResponse.getUser()).isNull();
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqualWhenAllFieldsMatch() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse response1 = AuthResponse.builder()
                .token("token123")
                .user(userResponse)
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("token123")
                .user(userResponse)
                .build();

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when tokens differ")
    void shouldNotBeEqualWhenTokensDiffer() {
        UserResponse userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        AuthResponse response1 = AuthResponse.builder()
                .token("token123")
                .user(userResponse)
                .build();

        AuthResponse response2 = AuthResponse.builder()
                .token("token456")
                .user(userResponse)
                .build();

        assertThat(response1).isNotEqualTo(response2);
    }
}