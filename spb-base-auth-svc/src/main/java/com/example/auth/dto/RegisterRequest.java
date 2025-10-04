package com.example.auth.dto;

import com.example.auth.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Date;

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
    private String phone;
    @NotBlank
    @Size(min = 8)
    private String password;
    @NotNull
    private String roleCode; // e.g., "USER", "ADMIN"
}