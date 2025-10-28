package com.synapse.money.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synapse.money.application.dto.request.LoginRequest;
import com.synapse.money.application.dto.request.RegisterRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import com.synapse.money.application.usecase.LoginUseCase;
import com.synapse.money.application.usecase.RegisterUseCase;
import com.synapse.money.domain.exception.EmailAlreadyExistsException;
import com.synapse.money.domain.exception.InvalidCredentialsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    private static final String VALID_FIRST_NAME = "John";
    private static final String VALID_LAST_NAME = "Doe";
    private static final String VALID_EMAIL = "john.doe@example.com";
    private static final String EXISTING_EMAIL = "existing@example.com";
    private static final String VALID_PASSWORD = "SecurePass123!";
    private static final String WRONG_PASSWORD = "WrongPassword";
    private static final String VALID_JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...";
    private static final String TOKEN_TYPE = "Bearer";
    private static final Long TOKEN_EXPIRATION = 3600L;
    private static final String EMPTY_STRING = "";
    private static final String INVALID_EMAIL = "invalid-email";
    private static final String SHORT_PASSWORD = "123";
    private static final String REGISTER_ENDPOINT = "/api/v1/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RegisterUseCase registerUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("POST /api/v1/auth/register - Should register user successfully")
    void shouldRegisterUserSuccessfully() throws Exception {
        RegisterRequest validRegisterRequest = new RegisterRequest(
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                VALID_EMAIL,
                VALID_PASSWORD
        );

        AuthResponse successfulAuthResponse = new AuthResponse(
                VALID_JWT_TOKEN,
                TOKEN_TYPE,
                TOKEN_EXPIRATION,
                null
        );

        when(registerUseCase.execute(any(RegisterRequest.class))).thenReturn(successfulAuthResponse);

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value(VALID_JWT_TOKEN))
                .andExpect(jsonPath("$.type").value(TOKEN_TYPE))
                .andExpect(jsonPath("$.expiresIn").value(TOKEN_EXPIRATION));
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 400 when email already exists")
    void shouldReturn400WhenEmailAlreadyExists() throws Exception {
        RegisterRequest duplicateEmailRequest = new RegisterRequest(
                VALID_FIRST_NAME,
                VALID_LAST_NAME,
                EXISTING_EMAIL,
                VALID_PASSWORD
        );

        when(registerUseCase.execute(any(RegisterRequest.class)))
                .thenThrow(new EmailAlreadyExistsException(EXISTING_EMAIL));

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmailRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/register - Should return 400 when request is invalid")
    void shouldReturn400WhenRegisterRequestIsInvalid() throws Exception {
        RegisterRequest invalidRegisterRequest = new RegisterRequest(
                EMPTY_STRING,
                VALID_LAST_NAME,
                INVALID_EMAIL,
                SHORT_PASSWORD
        );

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRegisterRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should login successfully")
    void shouldLoginSuccessfully() throws Exception {
        LoginRequest validLoginRequest = new LoginRequest(
                VALID_EMAIL,
                VALID_PASSWORD
        );

        AuthResponse successfulAuthResponse = new AuthResponse(
                VALID_JWT_TOKEN,
                TOKEN_TYPE,
                TOKEN_EXPIRATION,
                null
        );

        when(loginUseCase.execute(any(LoginRequest.class))).thenReturn(successfulAuthResponse);

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validLoginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(VALID_JWT_TOKEN))
                .andExpect(jsonPath("$.type").value(TOKEN_TYPE))
                .andExpect(jsonPath("$.expiresIn").value(TOKEN_EXPIRATION));
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 401 when credentials are invalid")
    void shouldReturn401WhenCredentialsAreInvalid() throws Exception {
        LoginRequest invalidCredentialsRequest = new LoginRequest(
                VALID_EMAIL,
                WRONG_PASSWORD
        );

        when(loginUseCase.execute(any(LoginRequest.class)))
                .thenThrow(new InvalidCredentialsException());

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCredentialsRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /api/v1/auth/login - Should return 400 when request is invalid")
    void shouldReturn400WhenLoginRequestIsInvalid() throws Exception {
        LoginRequest invalidLoginRequest = new LoginRequest(
                INVALID_EMAIL,
                EMPTY_STRING
        );

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidLoginRequest)))
                .andExpect(status().isBadRequest());
    }
}