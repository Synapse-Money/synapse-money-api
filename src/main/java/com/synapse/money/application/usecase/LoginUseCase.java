package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.request.LoginRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import com.synapse.money.application.mapper.UserResponseMapper;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.exception.InvalidCredentialsException;
import com.synapse.money.domain.repository.UserRepository;
import com.synapse.money.domain.service.PasswordHasher;
import com.synapse.money.domain.service.TokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;
    private final UserResponseMapper userResponseMapper;

    @Transactional(readOnly = true)
    public AuthResponse execute(LoginRequest request) {
        String normalizedEmail = request.email().toLowerCase();

        User user = userRepository.findByEmail(normalizedEmail)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));

        if (!passwordHasher.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException("Invalid email or password");
        }

        String token = tokenGenerator.generate(user);

        return new AuthResponse(
                token,
                userResponseMapper.toResponse(user)
        );
    }
}