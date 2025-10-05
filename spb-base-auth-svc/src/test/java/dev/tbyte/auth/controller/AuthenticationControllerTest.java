package dev.tbyte.auth.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.tbyte.auth.dto.LoginRequest;
import dev.tbyte.auth.dto.RegisterRequest;
import dev.tbyte.auth.entity.Gender;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.repository.RoleRepository;

import java.util.Date;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        Role userRole = new Role();
        userRole.setCode("USER");
        userRole.setName("User");
        roleRepository.save(userRole);
    }

    @Test
    void whenRegisterAndLoginWithCorrectCredentials_thenReturns200AndJwt() throws Exception {
        // Register a new user
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setConfirmPassword("password123");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setRoleCode("USER");
        registerRequest.setDob(new Date());
        registerRequest.setGender(Gender.MALE);
        registerRequest.setPhone("1234567890");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isOk());

        // Attempt to login
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("testuser@example.com");
        loginRequest.setPassword("password123");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isNotEmpty())
                .andExpect(jsonPath("$.refreshToken").isNotEmpty());
    }

    @Test
    void whenRegisterWithMismatchedPasswords_thenReturns400() throws Exception {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail("testuser@example.com");
        registerRequest.setPassword("password123");
        registerRequest.setConfirmPassword("password456");
        registerRequest.setFirstName("Test");
        registerRequest.setLastName("User");
        registerRequest.setRoleCode("USER");
        registerRequest.setDob(new Date());
        registerRequest.setGender(Gender.MALE);
        registerRequest.setPhone("1234567890");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.registerRequest").value("Passwords do not match. Please re-enter matching passwords."));
    }
}