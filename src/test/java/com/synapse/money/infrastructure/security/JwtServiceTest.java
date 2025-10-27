package com.synapse.money.infrastructure.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        String secret     = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";
        long   expiration = 86400000L;

        jwtService = new JwtService(secret, expiration);

        userDetails = User.builder()
                .username("john.doe@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();
    }

    @Test
    @DisplayName("Should generate valid JWT token")
    void shouldGenerateValidJwtToken() {
        String token = jwtService.generateToken(userDetails);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);
    }

    @Test
    @DisplayName("Should generate token with extra claims")
    void shouldGenerateTokenWithExtraClaims() {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("role", "ADMIN");
        extraClaims.put("userId", 123L);

        String token = jwtService.generateToken(extraClaims, userDetails);

        assertThat(token).isNotNull();
        assertThat(token).isNotEmpty();
        assertThat(token.split("\\.")).hasSize(3);

        String username = jwtService.extractUsername(token);
        assertThat(username).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should extract username from token")
    void shouldExtractUsernameFromToken() {
        String token = jwtService.generateToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("john.doe@example.com");
    }

    @Test
    @DisplayName("Should extract expiration date from token")
    void shouldExtractExpirationDateFromToken() {
        String token = jwtService.generateToken(userDetails);

        Date expiration = jwtService.extractExpiration(token);

        assertThat(expiration).isNotNull();
        assertThat(expiration).isAfter(new Date());
    }

    @Test
    @DisplayName("Should validate token successfully")
    void shouldValidateTokenSuccessfully() {
        String token = jwtService.generateToken(userDetails);

        boolean isValid = jwtService.isTokenValid(token, userDetails);

        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("Should invalidate token with wrong username")
    void shouldInvalidateTokenWithWrongUsername() {
        String token = jwtService.generateToken(userDetails);

        UserDetails differentUser = User.builder()
                .username("different@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        boolean isValid = jwtService.isTokenValid(token, differentUser);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false for expired token")
    void shouldReturnFalseForExpiredToken() {
        JwtService shortExpirationService = new JwtService(
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
                1L
        );

        String token = shortExpirationService.generateToken(userDetails);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        boolean isValid = shortExpirationService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should return false when username matches but token is expired")
    void shouldReturnFalseWhenUsernameMatchesButTokenIsExpired() {
        String token = jwtService.generateToken(userDetails);

        JwtService spyService = spy(jwtService);
        doReturn(true).when(spyService).isTokenExpired(token);

        boolean isValid = spyService.isTokenValid(token, userDetails);

        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("Should throw exception for malformed token")
    void shouldThrowExceptionForMalformedToken() {
        String malformedToken = "invalid.token.here";

        assertThatThrownBy(() -> jwtService.extractUsername(malformedToken))
                .isInstanceOf(MalformedJwtException.class);
    }

    @Test
    @DisplayName("Should throw exception for token with invalid signature")
    void shouldThrowExceptionForTokenWithInvalidSignature() {
        String token = jwtService.generateToken(userDetails);

        JwtService differentSecretService = new JwtService(
                "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437",
                86400000L
        );

        assertThatThrownBy(() -> differentSecretService.extractUsername(token))
                .isInstanceOf(SignatureException.class);
    }

    @Test
    @DisplayName("Should generate different tokens for different users")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        UserDetails user1 = User.builder()
                .username("user1@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        UserDetails user2 = User.builder()
                .username("user2@example.com")
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        String token1 = jwtService.generateToken(user1);
        String token2 = jwtService.generateToken(user2);

        assertThat(token1).isNotEqualTo(token2);
    }

    @Test
    @DisplayName("Should check if token is expired")
    void shouldCheckIfTokenIsExpired() {
        String token = jwtService.generateToken(userDetails);

        boolean isExpired = jwtService.isTokenExpired(token);

        assertThat(isExpired).isFalse();
    }

    @Test
    @DisplayName("Should check if token is expired for expired token")
    void shouldCheckIfTokenIsExpiredForExpiredToken() {
        JwtService shortExpirationService = new JwtService(
                "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970",
                1L
        );

        String token = shortExpirationService.generateToken(userDetails);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        assertThatThrownBy(() -> shortExpirationService.isTokenExpired(token))
                .isInstanceOf(ExpiredJwtException.class);
    }
}