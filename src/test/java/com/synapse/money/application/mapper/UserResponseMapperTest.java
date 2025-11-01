package com.synapse.money.application.mapper;

import com.synapse.money.application.dto.response.UserResponse;
import com.synapse.money.domain.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("UserResponseMapper Tests")
class UserResponseMapperTest {

    private UserResponseMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UserResponseMapper();
    }

    @Test
    @DisplayName("Should map User to UserResponse")
    void shouldMapUserToUserResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 15, 30);

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserResponse response = mapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.firstName()).isEqualTo("John");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo("john.doe@example.com");
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should not include password in response")
    void shouldNotIncludePasswordInResponse() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse response = mapper.toResponse(user);

        assertThat(response.toString()).doesNotContain("hashedPassword123");
        assertThat(response.toString()).doesNotContain("password");
    }

    @Test
    @DisplayName("Should not include updatedAt in response")
    void shouldNotIncludeUpdatedAtInResponse() {
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 10, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 15, 30);

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();

        UserResponse response = mapper.toResponse(user);

        assertThat(response.toString()).doesNotContain("updatedAt");
        assertThat(response.createdAt()).isEqualTo(createdAt);
    }

    @Test
    @DisplayName("Should throw exception when user is null")
    void shouldThrowExceptionWhenUserIsNull() {
        assertThatThrownBy(() -> mapper.toResponse(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User cannot be null");
    }

    @Test
    @DisplayName("Should handle user with null id")
    void shouldHandleUserWithNullId() {
        User user = User.builder()
                .id(null)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse response = mapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.id()).isNull();
    }

    @Test
    @DisplayName("Should handle user with null createdAt")
    void shouldHandleUserWithNullCreatedAt() {
        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(null)
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse response = mapper.toResponse(user);

        assertThat(response).isNotNull();
        assertThat(response.createdAt()).isNull();
    }

    @Test
    @DisplayName("Should map multiple users correctly")
    void shouldMapMultipleUsersCorrectly() {
        User user1 = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("password1")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User user2 = User.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .password("password2")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse response1 = mapper.toResponse(user1);
        UserResponse response2 = mapper.toResponse(user2);

        assertThat(response1.id()).isEqualTo(1L);
        assertThat(response1.firstName()).isEqualTo("John");
        assertThat(response2.id()).isEqualTo(2L);
        assertThat(response2.firstName()).isEqualTo("Jane");
    }
}