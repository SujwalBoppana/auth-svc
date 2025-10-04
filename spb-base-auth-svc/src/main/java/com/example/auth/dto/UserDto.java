package com.example.auth.dto;

import com.example.auth.entity.Gender;
import lombok.Data;

import java.util.Date;

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