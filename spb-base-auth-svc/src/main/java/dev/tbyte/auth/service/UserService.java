package dev.tbyte.auth.service;

import java.util.List;

import dev.tbyte.auth.dto.RegisterRequest;
import dev.tbyte.auth.dto.UserCreationRequest;
import dev.tbyte.auth.dto.UserDto;
import dev.tbyte.auth.entity.User;

public interface UserService {
    UserDto registerUser(RegisterRequest registerRequest);

    UserDto createUser(UserCreationRequest userCreationRequest);

    UserDto getUserById(Long id);

    List<UserDto> getAllUsers();

    UserDto updateUser(Long id, UserDto userDto);

    void deleteUser(Long id);

    User findByEmail(String email);

    UserDto getUserByEmail(String email);
}