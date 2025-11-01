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
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        AuthResponse authResponse = new AuthResponse(token, userResponse);

        assertThat(authResponse).isNotNull();
        assertThat(authResponse.token()).isEqualTo(token);
        assertThat(authResponse.user()).isEqualTo(userResponse);
    }

    @Test
    @DisplayName("Should contain user information")
    void shouldContainUserInformation() {
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        AuthResponse authResponse = new AuthResponse("token123", userResponse);

        assertThat(authResponse.user()).isNotNull();
        assertThat(authResponse.user().id()).isEqualTo(1L);
        assertThat(authResponse.user().firstName()).isEqualTo("John");
        assertThat(authResponse.user().lastName()).isEqualTo("Doe");
        assertThat(authResponse.user().email()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should contain JWT token")
    void shouldContainJwtToken() {
        String expectedToken =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIn0" +
                        ".dozjgNryP4J3jVmNHl0w5N_XgL0n3I9PlFUP0THsR8U";
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        AuthResponse authResponse = new AuthResponse(expectedToken, userResponse);

        assertThat(authResponse.token()).isNotNull();
        assertThat(authResponse.token()).isEqualTo(expectedToken);
        assertThat(authResponse.token()).startsWith("eyJ");
    }

    @Test
    @DisplayName("Should handle null token")
    void shouldHandleNullToken() {
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        AuthResponse authResponse = new AuthResponse(null, userResponse);

        assertThat(authResponse.token()).isNull();
        assertThat(authResponse.user()).isNotNull();
    }

    @Test
    @DisplayName("Should handle null user")
    void shouldHandleNullUser() {
        AuthResponse authResponse = new AuthResponse("token123", null);

        assertThat(authResponse.token()).isEqualTo("token123");
        assertThat(authResponse.user()).isNull();
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqualWhenAllFieldsMatch() {
        LocalDateTime now = LocalDateTime.now();
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                now
        );

        AuthResponse response1 = new AuthResponse("token123", userResponse);
        AuthResponse response2 = new AuthResponse("token123", userResponse);

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when tokens differ")
    void shouldNotBeEqualWhenTokensDiffer() {
        LocalDateTime now = LocalDateTime.now();
        UserResponse userResponse = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                now
        );

        AuthResponse response1 = new AuthResponse("token123", userResponse);
        AuthResponse response2 = new AuthResponse("token456", userResponse);

        assertThat(response1).isNotEqualTo(response2);
    }
}