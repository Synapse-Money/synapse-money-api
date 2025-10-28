package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.request.LoginRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import com.synapse.money.application.dto.response.UserResponse;
import com.synapse.money.application.mapper.UserResponseMapper;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.exception.InvalidCredentialsException;
import com.synapse.money.domain.repository.UserRepository;
import com.synapse.money.domain.service.PasswordHasher;
import com.synapse.money.domain.service.TokenGenerator;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginUseCase Tests")
class LoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private LoginRequest validRequest;
    private User existingUser;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        validRequest = LoginRequest.builder()
                .email("john.doe@example.com")
                .password("StrongPass123")
                .build();

        existingUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("$2a$10$hashedPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userResponse = UserResponse.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .createdAt(existingUser.getCreatedAt())
                .updatedAt(existingUser.getUpdatedAt())
                .build();
    }

    @Test
    @DisplayName("Should login user successfully with valid credentials")
    void shouldLoginUserSuccessfullyWithValidCredentials() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches(anyString(), anyString())).thenReturn(true);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponse);

        AuthResponse response = loginUseCase.execute(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt.token.here");
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo(1L);
        assertThat(response.getUser().getEmail()).isEqualTo("john.doe@example.com");

        verify(userRepository).findByEmail("john.doe@example.com");
        verify(passwordHasher).matches("StrongPass123", "$2a$10$hashedPassword");
        verify(tokenGenerator).generate(existingUser);
        verify(userResponseMapper).toResponse(existingUser);
    }

    @Test
    @DisplayName("Should throw exception when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> loginUseCase.execute(validRequest))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        verify(userRepository).findByEmail("john.doe@example.com");
        verify(passwordHasher, never()).matches(anyString(), anyString());
        verify(tokenGenerator, never()).generate(any(User.class));
    }

    @Test
    @DisplayName("Should throw exception when password does not match")
    void shouldThrowExceptionWhenPasswordDoesNotMatch() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches(anyString(), anyString())).thenReturn(false);

        assertThatThrownBy(() -> loginUseCase.execute(validRequest))
                .isInstanceOf(InvalidCredentialsException.class)
                .hasMessage("Invalid email or password");

        verify(userRepository).findByEmail("john.doe@example.com");
        verify(passwordHasher).matches("StrongPass123", "$2a$10$hashedPassword");
        verify(tokenGenerator, never()).generate(any(User.class));
    }

    @Test
    @DisplayName("Should normalize email before searching")
    void shouldNormalizeEmailBeforeSearching() {
        LoginRequest requestWithUpperCaseEmail = LoginRequest.builder()
                .email("JOHN.DOE@EXAMPLE.COM")
                .password("StrongPass123")
                .build();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches(anyString(), anyString())).thenReturn(true);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponse);

        loginUseCase.execute(requestWithUpperCaseEmail);

        verify(userRepository).findByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should generate token for authenticated user")
    void shouldGenerateTokenForAuthenticatedUser() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches(anyString(), anyString())).thenReturn(true);
        when(tokenGenerator.generate(existingUser)).thenReturn("generated.jwt.token");
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponse);

        AuthResponse response = loginUseCase.execute(validRequest);

        assertThat(response.getToken()).isEqualTo("generated.jwt.token");
        verify(tokenGenerator).generate(existingUser);
    }

    @Test
    @DisplayName("Should use PasswordHasher to verify password")
    void shouldUsePasswordHasherToVerifyPassword() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches("StrongPass123", "$2a$10$hashedPassword")).thenReturn(true);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");
        when(userResponseMapper.toResponse(any(User.class))).thenReturn(userResponse);

        loginUseCase.execute(validRequest);

        verify(passwordHasher).matches("StrongPass123", "$2a$10$hashedPassword");
    }

    @Test
    @DisplayName("Should return user data in response")
    void shouldReturnUserDataInResponse() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));
        when(passwordHasher.matches(anyString(), anyString())).thenReturn(true);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");
        when(userResponseMapper.toResponse(existingUser)).thenReturn(userResponse);

        AuthResponse response = loginUseCase.execute(validRequest);

        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo(1L);
        assertThat(response.getUser().getFirstName()).isEqualTo("John");
        assertThat(response.getUser().getLastName()).isEqualTo("Doe");
        assertThat(response.getUser().getEmail()).isEqualTo("john.doe@example.com");

        verify(userResponseMapper).toResponse(existingUser);
    }
}