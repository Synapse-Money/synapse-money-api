package com.synapse.money.presentation.controller;

import com.synapse.money.application.dto.response.UserProfileResponse;
import com.synapse.money.application.usecase.GetUserProfileUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final GetUserProfileUseCase getUserProfileUseCase;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getProfile(Authentication authentication) {
        String              email   = authentication.getName();
        UserProfileResponse profile = getUserProfileUseCase.execute(email);
        return ResponseEntity.ok(profile);
    }
}