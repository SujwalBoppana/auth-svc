package com.example.spbbaseauthsvc.controller;

import com.example.spbbaseauthsvc.dto.auth.JwtResponse;
import com.example.spbbaseauthsvc.dto.auth.LoginRequest;
import com.example.spbbaseauthsvc.dto.auth.RefreshTokenRequest;
import com.example.spbbaseauthsvc.dto.auth.RegisterRequest;
import com.example.spbbaseauthsvc.entity.User;
import com.example.spbbaseauthsvc.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for handling user authentication and registration.
 */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthService authService;

    public AuthenticationController(AuthService authService) {
        this.authService = authService;
    }

    /**
     * Registers a new user.
     *
     * @param registerRequest the registration details
     * @return the created user
     */
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        User registeredUser = authService.registerUser(registerRequest);
        return ResponseEntity.ok(registeredUser);
    }

    /**
     * Authenticates a user and returns a JWT.
     *
     * @param loginRequest the login credentials
     * @return a JWT response
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.loginUser(loginRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Refreshes an expired JWT.
     *
     * @param refreshTokenRequest the refresh token
     * @return a new JWT response
     */
    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        JwtResponse jwtResponse = authService.refreshToken(refreshTokenRequest);
        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * Retrieves the profile of the currently logged-in user.
     *
     * @return the user's profile
     */
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<User> getUserProfile() {
        User userProfile = authService.getLoggedInUserProfile();
        return ResponseEntity.ok(userProfile);
    }
}