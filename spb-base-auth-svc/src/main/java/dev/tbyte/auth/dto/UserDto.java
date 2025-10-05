package dev.tbyte.auth.dto;

import dev.tbyte.auth.entity.Gender;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import dev.tbyte.auth.validation.PasswordMatches;
import lombok.Data;

import java.util.Date;

@Data
@PasswordMatches
public class UserDto {
    private Long id;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String middleName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private Date dob;
    private Gender gender;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Confirm password is required")
    private String confirmPassword;

    private String roleCode;
}