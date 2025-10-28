package com.synapse.money.infrastructure.security;

import com.synapse.money.domain.service.PasswordHasher;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BCryptPasswordHasher implements PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String hash(String rawPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Raw password cannot be null");
        }
        if (rawPassword.isEmpty()) {
            throw new IllegalArgumentException("Raw password cannot be empty");
        }
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public boolean matches(
            String rawPassword,
            String hashedPassword) {
        if (rawPassword == null) {
            throw new IllegalArgumentException("Raw password cannot be null");
        }
        if (hashedPassword == null) {
            throw new IllegalArgumentException("Hashed password cannot be null");
        }
        return passwordEncoder.matches(rawPassword, hashedPassword);
    }
}