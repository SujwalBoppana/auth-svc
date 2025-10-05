package dev.tbyte.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AdminPasswordResetRequest {
    @NotNull
    private Long userId;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}