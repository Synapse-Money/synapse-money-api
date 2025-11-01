package com.synapse.money.application.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserResponse Tests")
class UserResponseTest {

    @Test
    @DisplayName("Should create valid UserResponse with all fields")
    void shouldCreateValidUserResponse() {
        Long          id        = 1L;
        String        firstName = "John";
        String        lastName  = "Doe";
        String        email     = "john.doe@example.com";
        LocalDateTime createdAt = LocalDateTime.now();

        UserResponse response = new UserResponse(
                id,
                firstName,
                lastName,
                email,
                createdAt
        );

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        assertThat(response.firstName()).isEqualTo(firstName);
        assertThat(response.lastName()).isEqualTo(lastName);
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should not have updatedAt field")
    void shouldNotHaveUpdatedAtField() {
        UserResponse response = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        assertThat(response.toString()).doesNotContain("updatedAt");
    }

    @Test
    @DisplayName("Should not have password field")
    void shouldNotHavePasswordField() {
        UserResponse response = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        assertThat(response.toString()).doesNotContain("password");
    }

    @Test
    @DisplayName("Should handle null createdAt")
    void shouldHandleNullCreatedAt() {
        UserResponse response = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                null
        );

        assertThat(response.createdAt()).isNull();
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqualWhenAllFieldsMatch() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response1 = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                now
        );

        UserResponse response2 = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                now
        );

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
        UserResponse response1 = new UserResponse(
                1L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        UserResponse response2 = new UserResponse(
                2L,
                "John",
                "Doe",
                "john.doe@example.com",
                LocalDateTime.now()
        );

        assertThat(response1).isNotEqualTo(response2);
    }
}