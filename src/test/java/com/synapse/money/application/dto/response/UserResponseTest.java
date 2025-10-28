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

        UserResponse response = UserResponse.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .createdAt(createdAt)
                .build();

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getFirstName()).isEqualTo(firstName);
        assertThat(response.getLastName()).isEqualTo(lastName);
        assertThat(response.getEmail()).isEqualTo(email);
        assertThat(response.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should not have updatedAt field")
    void shouldNotHaveUpdatedAtField() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(response.toString()).doesNotContain("updatedAt");
    }

    @Test
    @DisplayName("Should not have password field")
    void shouldNotHavePasswordField() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(LocalDateTime.now())
                .build();

        assertThat(response.toString()).doesNotContain("password");
    }

    @Test
    @DisplayName("Should handle null createdAt")
    void shouldHandleNullCreatedAt() {
        UserResponse response = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(null)
                .build();

        assertThat(response.getCreatedAt()).isNull();
    }

    @Test
    @DisplayName("Should be equal when all fields match")
    void shouldBeEqualWhenAllFieldsMatch() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response1 = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .build();

        UserResponse response2 = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .build();

        assertThat(response1).isEqualTo(response2);
        assertThat(response1.hashCode()).isEqualTo(response2.hashCode());
    }

    @Test
    @DisplayName("Should not be equal when ids differ")
    void shouldNotBeEqualWhenIdsDiffer() {
        LocalDateTime now = LocalDateTime.now();

        UserResponse response1 = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .build();

        UserResponse response2 = UserResponse.builder()
                .id(2L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(now)
                .build();

        assertThat(response1).isNotEqualTo(response2);
    }
}