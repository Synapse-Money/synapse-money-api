package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.request.RegisterRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import com.synapse.money.application.mapper.UserResponseMapper;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.exception.EmailAlreadyExistsException;
import com.synapse.money.domain.repository.UserRepository;
import com.synapse.money.domain.service.PasswordHasher;
import com.synapse.money.domain.service.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RegisterUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;
    private final UserResponseMapper userResponseMapper;

    @Transactional
    public AuthResponse execute(RegisterRequest request) {
        String normalizedEmail = request.getEmail().toLowerCase();

        if (userRepository.existsByEmail(normalizedEmail)) {
            throw new EmailAlreadyExistsException("Email already exists: " + normalizedEmail);
        }

        String hashedPassword = passwordHasher.hash(request.getPassword());

        LocalDateTime now = LocalDateTime.now();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(normalizedEmail)
                .password(hashedPassword)
                .createdAt(now)
                .updatedAt(now)
                .build();

        User savedUser = userRepository.save(user);

        String token = tokenGenerator.generate(savedUser);

        return AuthResponse.builder()
                .token(token)
                .user(userResponseMapper.toResponse(savedUser))
                .build();
    }
}