package com.synapse.money.application.dto.response;

import java.time.LocalDateTime;

public record UserProfileResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        LocalDateTime createdAt
) {}