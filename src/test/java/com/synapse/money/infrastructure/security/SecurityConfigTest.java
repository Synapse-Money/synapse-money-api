package com.synapse.money.infrastructure.security;

import com.synapse.money.application.usecase.LoginUseCase;
import com.synapse.money.application.usecase.RegisterUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@DisplayName("SecurityConfig Tests")
class SecurityConfigTest {

    private static final String REGISTER_ENDPOINT = "/api/v1/auth/register";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/login";
    private static final String PROTECTED_ENDPOINT = "/api/v1/users";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RegisterUseCase registerUseCase;

    @MockitoBean
    private LoginUseCase loginUseCase;

    @Test
    @DisplayName("Should allow public access to register endpoint without authentication")
    void shouldAllowPublicAccessToRegisterEndpoint() throws Exception {
        String registerRequest = """
                                 {
                                     "firstName": "John",
                                     "lastName": "Doe",
                                     "email": "john.doe@example.com",
                                     "password": "SecurePass123!"
                                 }
                                 """;

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should allow public access to login endpoint without authentication")
    void shouldAllowPublicAccessToLoginEndpoint() throws Exception {
        String loginRequest = """
                              {
                                  "email": "john.doe@example.com",
                                  "password": "SecurePass123!"
                              }
                              """;

        mockMvc.perform(post(LOGIN_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(loginRequest))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should require authentication for protected endpoints")
    void shouldRequireAuthenticationForProtectedEndpoints() throws Exception {
        mockMvc.perform(get(PROTECTED_ENDPOINT))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should allow access without Authorization header to public endpoints")
    void shouldAllowAccessWithoutAuthorizationHeaderToPublicEndpoints() throws Exception {
        String registerRequest = """
                                 {
                                     "firstName": "Jane",
                                     "lastName": "Smith",
                                     "email": "jane.smith@example.com",
                                     "password": "AnotherPass456!"
                                 }
                                 """;

        mockMvc.perform(post(REGISTER_ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(registerRequest))
                .andExpect(status().isCreated());
    }
}