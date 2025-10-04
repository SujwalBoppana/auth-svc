package dev.tbyte.auth.dto;

import java.util.Date;

import dev.tbyte.auth.entity.Gender;
import lombok.Data;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private Date dob;
    private Gender gender;
    private String email;
    private String phone;
    private Long roleId;
}