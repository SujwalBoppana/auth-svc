package com.example.auth.service;

import com.example.auth.dto.UserDto;
import com.example.auth.entity.User;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    UserDto updateUser(Long id, UserDto userDto);
    void deleteUser(Long id);
    User findByEmail(String email);
}