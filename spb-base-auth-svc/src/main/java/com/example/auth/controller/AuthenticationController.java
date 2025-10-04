package com.example.auth.controller;

import com.example.auth.dto.*;
import com.example.auth.entity.Role;
import com.example.auth.entity.User;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.repository.RoleRepository;
import com.example.auth.repository.UserRepository;
import com.example.auth.service.CustomUserDetailsService;
import com.example.auth.service.UserService;
import com.example.auth.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Error: Email is already in use!");
        }

        User user = new User();
        user.setFirstName(registerRequest.getFirstName());
        user.setMiddleName(registerRequest.getMiddleName());
        user.setLastName(registerRequest.getLastName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setDob(registerRequest.getDob());
        user.setGender(registerRequest.getGender());
        user.setPhone(registerRequest.getPhone());

        Role userRole = roleRepository.findByCode(registerRequest.getRoleCode())
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + registerRequest.getRoleCode()));
        user.setRole(userRole);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginRequest.getEmail());
        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword() {
        return ResponseEntity.ok("Forgot password flow is not implemented yet");
    }

    @PostMapping("/refresh")
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        String userEmail = jwtUtil.extractUsername(refreshTokenRequest.getRefreshToken());
        UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

        if (jwtUtil.validateToken(refreshTokenRequest.getRefreshToken(), userDetails)) {
            String newAccessToken = jwtUtil.generateToken(userDetails);
            return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshTokenRequest.getRefreshToken()));
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/profile")
    @SecurityRequirement(name = "BearerAuth")
    public ResponseEntity<UserDto> getProfile(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.findByEmail(email);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setMiddleName(user.getMiddleName());
        userDto.setLastName(user.getLastName());
        userDto.setDob(user.getDob());
        userDto.setGender(user.getGender());
        userDto.setEmail(user.getEmail());
        userDto.setPhone(user.getPhone());
        userDto.setRoleId(user.getRole().getId());
        return ResponseEntity.ok(userDto);
    }
}