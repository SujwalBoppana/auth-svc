package dev.tbyte.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String newPassword;

    @NotBlank
    private String confirmPassword;
}