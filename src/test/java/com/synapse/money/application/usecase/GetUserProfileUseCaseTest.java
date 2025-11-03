package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.response.UserProfileResponse;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.repository.UserRepository;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetUserProfileUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserProfileUseCase getUserProfileUseCase;

    @Test
    @DisplayName("Should return user profile when user exists")
    void shouldReturnUserProfileWhenUserExists() {
        String        email = "john.doe@test.com";
        LocalDateTime now   = LocalDateTime.now();

        User user = User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .email(email)
                .password("hashedPassword")
                .createdAt(now)
                .updatedAt(now)
                .build();

        when(userRepository.findByEmail(email))
                .thenReturn(Optional.of(user));

        UserProfileResponse response = getUserProfileUseCase.execute(email);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.firstName()).isEqualTo("John");
        assertThat(response.lastName()).isEqualTo("Doe");
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.createdAt()).isEqualTo(now);

        verify(userRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when user not found")
    void shouldThrowExceptionWhenUserNotFound() {
        String nonExistentEmail = "nonexistent@test.com";

        when(userRepository.findByEmail(nonExistentEmail))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> getUserProfileUseCase.execute(nonExistentEmail))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");

        verify(userRepository).findByEmail(nonExistentEmail);
    }
}