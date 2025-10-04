package com.example.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO for requesting a new JWT using a refresh token.
 */
@Data
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
}