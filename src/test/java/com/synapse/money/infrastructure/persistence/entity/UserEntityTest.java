package com.synapse.money.infrastructure.persistence.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("UserEntity Tests")
class UserEntityTest {

    @Test
    @DisplayName("Should create user entity with all fields")
    void shouldCreateUserEntityWithAllFields() {
        LocalDateTime now = LocalDateTime.now();

        UserEntity entity = UserEntity.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertThat(entity.getId()).isEqualTo(1L);
        assertThat(entity.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(entity.getPassword()).isEqualTo("hashedPassword123");
        assertThat(entity.getFirstName()).isEqualTo("John");
        assertThat(entity.getLastName()).isEqualTo("Doe");
        assertThat(entity.getCreatedAt()).isEqualTo(now);
        assertThat(entity.getUpdatedAt()).isEqualTo(now);
    }

    @Test
    @DisplayName("Should handle prePersist callback")
    void shouldHandlePrePersistCallback() {
        UserEntity entity = UserEntity.builder()
                .email("test@example.com")
                .password("password")
                .firstName("Test")
                .lastName("User")
                .build();

        entity.prePersist();

        assertThat(entity.getCreatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Should handle preUpdate callback")
    void shouldHandlePreUpdateCallback() {
        LocalDateTime originalCreatedAt = LocalDateTime.now().minusDays(1);

        UserEntity entity = UserEntity.builder()
                .createdAt(originalCreatedAt)
                .build();

        entity.preUpdate();

        assertThat(entity.getCreatedAt()).isEqualTo(originalCreatedAt);
        assertThat(entity.getUpdatedAt()).isNotNull();
        assertThat(entity.getUpdatedAt()).isAfter(originalCreatedAt);
    }
}