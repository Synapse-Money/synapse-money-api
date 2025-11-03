package com.synapse.money.presentation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.synapse.money.application.dto.request.LoginRequest;
import com.synapse.money.application.dto.request.RegisterRequest;
import com.synapse.money.application.dto.response.AuthResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Sql(scripts = "/sql/clean-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Order(1)
    @DisplayName("Should register, login and access protected endpoint")
    void shouldRegisterLoginAndAccessProtectedEndpoint() throws Exception {
        String uniqueEmail = "user-" + UUID.randomUUID() + "@test.com";
        String password    = "Password123!";

        RegisterRequest registerRequest = new RegisterRequest(
                "John",
                "Doe",
                uniqueEmail,
                password
        );

        MvcResult registerResult = mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value(uniqueEmail))
                .andReturn();

        String       registerResponseBody = registerResult.getResponse().getContentAsString();
        AuthResponse registerResponse     = objectMapper.readValue(registerResponseBody, AuthResponse.class);
        String       registerToken        = registerResponse.token();

        assertThat(registerToken).isNotBlank();

        LoginRequest loginRequest = new LoginRequest(uniqueEmail, password);

        MvcResult loginResult = mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.user.email").value(uniqueEmail))
                .andReturn();

        String       loginResponseBody = loginResult.getResponse().getContentAsString();
        AuthResponse loginResponse     = objectMapper.readValue(loginResponseBody, AuthResponse.class);
        String       loginToken        = loginResponse.token();

        assertThat(loginToken).isNotBlank();

        mockMvc.perform(get("/api/v1/users/profile")
                        .header("Authorization", "Bearer " + loginToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(uniqueEmail));
    }

    @Test
    @Order(2)
    @DisplayName("Should return 403 when accessing protected endpoint without token")
    void shouldReturn403WhenAccessingProtectedEndpointWithoutToken() throws Exception {
        mockMvc.perform(get("/api/v1/users/profile"))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(3)
    @DisplayName("Should return 403 when accessing protected endpoint with invalid token")
    void shouldReturn403WhenAccessingProtectedEndpointWithInvalidToken() throws Exception {
        String invalidToken =
                "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJ0ZXN0QGV4YW1wbGUuY29tIn0.invalid_signature";

        mockMvc.perform(get("/api/v1/users/profile")
                        .header("Authorization", "Bearer " + invalidToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @Order(4)
    @DisplayName("Should return 401 when login with wrong password")
    void shouldReturn401WhenLoginWithWrongPassword() throws Exception {
        String uniqueEmail     = "user-" + UUID.randomUUID() + "@test.com";
        String correctPassword = "Password123!";
        String wrongPassword   = "WrongPassword123!";

        RegisterRequest registerRequest = new RegisterRequest(
                "Jane",
                "Doe",
                uniqueEmail,
                correctPassword
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isCreated());

        LoginRequest loginRequest = new LoginRequest(uniqueEmail, wrongPassword);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }

    @Test
    @Order(5)
    @DisplayName("Should return 409 when registering with duplicate email")
    void shouldReturn409WhenRegisteringWithDuplicateEmail() throws Exception {
        String duplicateEmail = "duplicate-" + UUID.randomUUID() + "@test.com";
        String password       = "Password123!";

        RegisterRequest firstRequest = new RegisterRequest(
                "First",
                "User",
                duplicateEmail,
                password
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(firstRequest)))
                .andExpect(status().isCreated());

        RegisterRequest duplicateRequest = new RegisterRequest(
                "Second",
                "User",
                duplicateEmail,
                password
        );

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateRequest)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @Order(6)
    @DisplayName("Should return 401 when login with non-existent email")
    void shouldReturn401WhenLoginWithNonExistentEmail() throws Exception {
        String nonExistentEmail = "nonexistent-" + UUID.randomUUID() + "@test.com";

        LoginRequest loginRequest = new LoginRequest(nonExistentEmail, "Password123!");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.message").value("Invalid email or password"));
    }
}