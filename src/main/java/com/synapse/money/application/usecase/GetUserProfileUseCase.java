package com.synapse.money.application.usecase;

import com.synapse.money.application.dto.response.UserProfileResponse;
import com.synapse.money.domain.entity.User;
import com.synapse.money.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GetUserProfileUseCase {

    private final UserRepository userRepository;

    public UserProfileResponse execute(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return new UserProfileResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}