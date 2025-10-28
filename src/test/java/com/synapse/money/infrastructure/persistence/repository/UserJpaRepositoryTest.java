package com.synapse.money.infrastructure.persistence.repository;

import com.synapse.money.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DisplayName("UserJpaRepository Integration Tests")
class UserJpaRepositoryTest {

    private final UserJpaRepository userJpaRepository;
    private final TestEntityManager entityManager;

    @Autowired
    UserJpaRepositoryTest(
            UserJpaRepository userJpaRepository,
            TestEntityManager entityManager) {
        this.userJpaRepository = userJpaRepository;
        this.entityManager = entityManager;
    }

    @Test
    @DisplayName("Should save user entity successfully")
    void shouldSaveUserEntitySuccessfully() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = userJpaRepository.save(user);

        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(savedUser.getFirstName()).isEqualTo("John");
        assertThat(savedUser.getLastName()).isEqualTo("Doe");
    }

    @Test
    @DisplayName("Should find user by email ignoring case")
    void shouldFindUserByEmailIgnoringCase() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(user);

        Optional<UserEntity> foundUser = userJpaRepository.findByEmailIgnoreCase("JOHN.DOE@EXAMPLE.COM");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        Optional<UserEntity> foundUser = userJpaRepository.findByEmailIgnoreCase("nonexistent@example.com");

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should check if email exists ignoring case")
    void shouldCheckIfEmailExistsIgnoringCase() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(user);

        boolean exists = userJpaRepository.existsByEmailIgnoreCase("JOHN.DOE@EXAMPLE.COM");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Should return false when email does not exist")
    void shouldReturnFalseWhenEmailDoesNotExist() {
        boolean exists = userJpaRepository.existsByEmailIgnoreCase("nonexistent@example.com");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Should find user by exact email")
    void shouldFindUserByExactEmail() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        entityManager.persistAndFlush(user);

        Optional<UserEntity> foundUser = userJpaRepository.findByEmailIgnoreCase("john.doe@example.com");

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(user.getId());
    }

    @Test
    @DisplayName("Should update user successfully")
    void shouldUpdateUserSuccessfully() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = entityManager.persistAndFlush(user);

        savedUser.setFirstName("Jane");
        savedUser.setUpdatedAt(LocalDateTime.now());

        UserEntity updatedUser = userJpaRepository.save(savedUser);

        assertThat(updatedUser.getFirstName()).isEqualTo("Jane");
        assertThat(updatedUser.getId()).isEqualTo(savedUser.getId());
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {
        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        UserEntity savedUser = entityManager.persistAndFlush(user);

        Optional<UserEntity> foundUser = userJpaRepository.findById(savedUser.getId());

        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getEmail()).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should return empty when user not found by id")
    void shouldReturnEmptyWhenUserNotFoundById() {
        Optional<UserEntity> foundUser = userJpaRepository.findById(999L);

        assertThat(foundUser).isEmpty();
    }

    @Test
    @DisplayName("Should persist timestamps correctly")
    void shouldPersistTimestampsCorrectly() {
        LocalDateTime now = LocalDateTime.now();

        UserEntity user = UserEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .createdAt(now)
                .updatedAt(now)
                .build();

        UserEntity savedUser = userJpaRepository.save(user);

        assertThat(savedUser.getCreatedAt()).isNotNull();
        assertThat(savedUser.getUpdatedAt()).isNotNull();
        assertThat(savedUser.getCreatedAt()).isEqualToIgnoringNanos(now);
        assertThat(savedUser.getUpdatedAt()).isEqualToIgnoringNanos(now);
    }
}