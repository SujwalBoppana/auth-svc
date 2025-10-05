package dev.tbyte.auth.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.tbyte.auth.dto.ForgotPasswordRequest;
import dev.tbyte.auth.dto.JwtResponse;
import dev.tbyte.auth.dto.LoginRequest;
import dev.tbyte.auth.dto.RefreshTokenRequest;
import dev.tbyte.auth.dto.RegisterRequest;
import dev.tbyte.auth.dto.UserDto;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.entity.User;
import dev.tbyte.auth.exception.ResourceNotFoundException;
import dev.tbyte.auth.repository.RoleRepository;
import dev.tbyte.auth.repository.UserRepository;
import dev.tbyte.auth.service.CustomUserDetailsService;
import dev.tbyte.auth.service.UserService;
import dev.tbyte.auth.util.JwtUtil;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Role not found with code: " + registerRequest.getRoleCode()));
        user.setRole(userRole);

        userRepository.save(user);

        return ResponseEntity.ok("User registered successfully!");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        final UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        final String accessToken = jwtUtil.generateToken(userDetails);
        final String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(accessToken, refreshToken));
    }

    @PostMapping("/forgot")
    public ResponseEntity<String> forgotPassword(@Valid @RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        userService.forgotPassword(forgotPasswordRequest);
        return ResponseEntity.ok("Password has been reset successfully");
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
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }
}