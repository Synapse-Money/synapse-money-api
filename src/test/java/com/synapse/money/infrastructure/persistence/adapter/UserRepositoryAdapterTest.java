package com.synapse.money.infrastructure.persistence.adapter;

import com.synapse.money.domain.entity.User;
import com.synapse.money.infrastructure.persistence.entity.UserEntity;
import com.synapse.money.infrastructure.persistence.mapper.UserMapper;
import com.synapse.money.infrastructure.persistence.repository.UserJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserRepositoryAdapter Tests")
class UserRepositoryAdapterTest {

    @Mock
    private UserJpaRepository jpaRepository;

    @Mock
    private UserMapper mapper;

    @InjectMocks
    private UserRepositoryAdapter adapter;

    private User domainUser;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        LocalDateTime now = LocalDateTime.now();

        domainUser = User.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        userEntity = UserEntity.builder()
                .id(1L)
                .email("john.doe@example.com")
                .password("hashedPassword123")
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    @Test
    @DisplayName("Should save user")
    void shouldSaveUser() {
        when(mapper.toEntity(domainUser)).thenReturn(userEntity);
        when(jpaRepository.save(userEntity)).thenReturn(userEntity);
        when(mapper.toDomain(userEntity)).thenReturn(domainUser);

        User result = adapter.save(domainUser);

        assertThat(result).isEqualTo(domainUser);
        verify(mapper).toEntity(domainUser);
        verify(jpaRepository).save(userEntity);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should find user by email")
    void shouldFindUserByEmail() {
        String email = "john.doe@example.com";
        when(jpaRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(domainUser);

        Optional<User> result = adapter.findByEmail(email);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domainUser);
        verify(jpaRepository).findByEmailIgnoreCase(email);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should return empty when user not found by email")
    void shouldReturnEmptyWhenUserNotFoundByEmail() {
        String email = "notfound@example.com";
        when(jpaRepository.findByEmailIgnoreCase(email)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findByEmail(email);

        assertThat(result).isEmpty();
        verify(jpaRepository).findByEmailIgnoreCase(email);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should find user by id")
    void shouldFindUserById() {
        Long id = 1L;
        when(jpaRepository.findById(id)).thenReturn(Optional.of(userEntity));
        when(mapper.toDomain(userEntity)).thenReturn(domainUser);

        Optional<User> result = adapter.findById(id);

        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(domainUser);
        verify(jpaRepository).findById(id);
        verify(mapper).toDomain(userEntity);
    }

    @Test
    @DisplayName("Should return empty when user not found by id")
    void shouldReturnEmptyWhenUserNotFoundById() {
        Long id = 999L;
        when(jpaRepository.findById(id)).thenReturn(Optional.empty());

        Optional<User> result = adapter.findById(id);

        assertThat(result).isEmpty();
        verify(jpaRepository).findById(id);
        verify(mapper, never()).toDomain(any());
    }

    @Test
    @DisplayName("Should check if user exists by email")
    void shouldCheckIfUserExistsByEmail() {
        String email = "john.doe@example.com";
        when(jpaRepository.existsByEmailIgnoreCase(email)).thenReturn(true);

        boolean result = adapter.existsByEmail(email);

        assertThat(result).isTrue();
        verify(jpaRepository).existsByEmailIgnoreCase(email);
    }

    @Test
    @DisplayName("Should return false when user does not exist by email")
    void shouldReturnFalseWhenUserDoesNotExistByEmail() {
        String email = "notfound@example.com";
        when(jpaRepository.existsByEmailIgnoreCase(email)).thenReturn(false);

        boolean result = adapter.existsByEmail(email);

        assertThat(result).isFalse();
        verify(jpaRepository).existsByEmailIgnoreCase(email);
    }
}