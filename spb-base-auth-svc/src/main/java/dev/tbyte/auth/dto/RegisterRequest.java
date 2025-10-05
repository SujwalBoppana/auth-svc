package dev.tbyte.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

import dev.tbyte.auth.entity.Gender;

@Data
public class RegisterRequest {
    @NotBlank
    private String firstName;
    private String middleName;
    @NotBlank
    private String lastName;
    private Date dob;
    private Gender gender;
    @NotBlank
    @Email
    private String email;
    @Pattern(regexp = "^\\d{10}$")
    private String phone;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotNull
    private String roleCode; // e.g., "USER", "ADMIN"
}