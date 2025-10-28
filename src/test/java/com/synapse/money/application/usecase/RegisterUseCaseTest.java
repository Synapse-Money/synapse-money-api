package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.request.RegisterRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import com.synapse.money.application.mapper.UserResponseMapper;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.exception.EmailAlreadyExistsException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("RegisterUseCase Tests")
class RegisterUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @Mock
    private TokenGenerator tokenGenerator;

    @Mock
    private UserResponseMapper userResponseMapper;

    @InjectMocks
    private RegisterUseCase registerUseCase;

    private RegisterRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("StrongPass123")
                .build();
    }

    @Test
    @DisplayName("Should register user successfully")
    void shouldRegisterUserSuccessfully() {
        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");

        when(userResponseMapper.toResponse(any(User.class))).thenReturn(
                com.synapse.money.application.dto.response.UserResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .createdAt(savedUser.getCreatedAt())
                        .build()
        );

        AuthResponse response = registerUseCase.execute(validRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("jwt.token.here");
        assertThat(response.getUser()).isNotNull();
        assertThat(response.getUser().getId()).isEqualTo(1L);
        assertThat(response.getUser().getEmail()).isEqualTo("john.doe@example.com");

        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(passwordHasher).hash("StrongPass123");
        verify(userRepository).save(any(User.class));
        verify(tokenGenerator).generate(savedUser);
        verify(userResponseMapper).toResponse(savedUser);
    }

    @Test
    @DisplayName("Should throw exception when email already exists")
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> registerUseCase.execute(validRequest))
                .isInstanceOf(EmailAlreadyExistsException.class)
                .hasMessage("Email already exists: john.doe@example.com");

        verify(userRepository).existsByEmail("john.doe@example.com");
        verify(passwordHasher, never()).hash(anyString());
        verify(userRepository, never()).save(any(User.class));
        verify(tokenGenerator, never()).generate(any(User.class));
    }

    @Test
    @DisplayName("Should normalize email before checking existence")
    void shouldNormalizeEmailBeforeCheckingExistence() {
        RegisterRequest requestWithUpperCaseEmail = RegisterRequest.builder()
                .firstName("John")
                .lastName("Doe")
                .email("JOHN.DOE@EXAMPLE.COM")
                .password("StrongPass123")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");

        when(userResponseMapper.toResponse(any(User.class))).thenReturn(
                com.synapse.money.application.dto.response.UserResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .createdAt(savedUser.getCreatedAt())
                        .build()
        );

        registerUseCase.execute(requestWithUpperCaseEmail);

        verify(userRepository).existsByEmail("john.doe@example.com");
    }

    @Test
    @DisplayName("Should hash password before saving")
    void shouldHashPasswordBeforeSaving() {
        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordHasher.hash("StrongPass123")).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");

        when(userResponseMapper.toResponse(any(User.class))).thenReturn(
                com.synapse.money.application.dto.response.UserResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .createdAt(savedUser.getCreatedAt())
                        .build()
        );

        registerUseCase.execute(validRequest);

        verify(passwordHasher).hash("StrongPass123");
        verify(userRepository).save(argThat(user ->
                user.getPassword().equals("hashedPassword")
        ));
    }

    @Test
    @DisplayName("Should generate token for registered user")
    void shouldGenerateTokenForRegisteredUser() {
        User savedUser = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .password("hashedPassword")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(tokenGenerator.generate(savedUser)).thenReturn("generated.jwt.token");

        when(userResponseMapper.toResponse(any(User.class))).thenReturn(
                com.synapse.money.application.dto.response.UserResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .createdAt(savedUser.getCreatedAt())
                        .build()
        );

        AuthResponse response = registerUseCase.execute(validRequest);

        assertThat(response.getToken()).isEqualTo("generated.jwt.token");
        verify(tokenGenerator).generate(savedUser);
    }

    @Test
    @DisplayName("Should set timestamps when creating user")
    void shouldSetTimestampsWhenCreatingUser() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordHasher.hash(anyString())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return User.builder()
                    .id(1L)
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .password(user.getPassword())
                    .createdAt(user.getCreatedAt())
                    .updatedAt(user.getUpdatedAt())
                    .build();
        });
        when(tokenGenerator.generate(any(User.class))).thenReturn("jwt.token.here");

        when(userResponseMapper.toResponse(any(User.class))).thenReturn(
                com.synapse.money.application.dto.response.UserResponse.builder()
                        .id(1L)
                        .firstName("John")
                        .lastName("Doe")
                        .email("john.doe@example.com")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        registerUseCase.execute(validRequest);

        verify(userRepository).save(argThat(user ->
                user.getCreatedAt() != null && user.getUpdatedAt() != null
        ));
    }
}