package dev.tbyte.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import dev.tbyte.auth.dto.RegisterRequest;
import dev.tbyte.auth.dto.UserCreationRequest;
import dev.tbyte.auth.dto.UserDto;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.entity.User;
import dev.tbyte.auth.exception.ResourceNotFoundException;
import dev.tbyte.auth.repository.RoleRepository;
import dev.tbyte.auth.repository.UserRepository;
import dev.tbyte.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @org.springframework.beans.factory.annotation.Value("${auth.password.salt}")
    private String salt;

    @Override
    public UserDto registerUser(RegisterRequest registerRequest) {
        User user = toEntity(registerRequest);
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword() + salt));
        Long currentUserId = SecurityUtil.getCurrentUserId();
        user.setCreatedBy(currentUserId);
        user.setUpdatedBy(currentUserId);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    public UserDto createUser(UserCreationRequest userCreationRequest) {
        User user = toEntity(userCreationRequest);
        user.setPassword(passwordEncoder.encode(userCreationRequest.getPassword() + salt));
        Long currentUserId = SecurityUtil.getCurrentUserId();
        user.setCreatedBy(currentUserId);
        user.setUpdatedBy(currentUserId);
        User savedUser = userRepository.save(user);
        return toDto(savedUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return toDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));

        existingUser.setFirstName(userDto.getFirstName());
        existingUser.setMiddleName(userDto.getMiddleName());
        existingUser.setLastName(userDto.getLastName());
        existingUser.setDob(userDto.getDob());
        existingUser.setGender(userDto.getGender());
        existingUser.setEmail(userDto.getEmail());
        existingUser.setPhone(userDto.getPhone());
        existingUser.setUpdatedBy(SecurityUtil.getCurrentUserId());

        if (userDto.getRoleCode() != null) {
            Role role = roleRepository.findByCode(userDto.getRoleCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + userDto.getRoleCode()));
            existingUser.setRole(role);
        }

        User updatedUser = userRepository.save(existingUser);
        return toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        user.setDeleted(true);
        user.setUpdatedBy(SecurityUtil.getCurrentUserId());
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with email: " + email));
    }

    @Override
    public UserDto getUserByEmail(String email) {
        User user = findByEmail(email);
        return toDto(user);
    }

    private UserDto toDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        dto.setCreatedBy(user.getCreatedBy());
        dto.setUpdatedBy(user.getUpdatedBy());
        dto.setDeleted(user.isDeleted());
        dto.setFirstName(user.getFirstName());
        dto.setMiddleName(user.getMiddleName());
        dto.setLastName(user.getLastName());
        dto.setDob(user.getDob());
        dto.setGender(user.getGender());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        if (user.getRole() != null) {
            dto.setRoleCode(user.getRole().getCode());
        }
        return dto;
    }

    private User toEntity(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getRoleCode() != null) {
            Role role = roleRepository.findByCode(dto.getRoleCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + dto.getRoleCode()));
            user.setRole(role);
        }
        return user;
    }

    private User toEntity(UserCreationRequest dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getRoleCode() != null) {
            Role role = roleRepository.findByCode(dto.getRoleCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + dto.getRoleCode()));
            user.setRole(role);
        }
        return user;
    }

    private User toEntity(RegisterRequest dto) {
        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setMiddleName(dto.getMiddleName());
        user.setLastName(dto.getLastName());
        user.setDob(dto.getDob());
        user.setGender(dto.getGender());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

        if (dto.getRoleCode() != null) {
            Role role = roleRepository.findByCode(dto.getRoleCode())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + dto.getRoleCode()));
            user.setRole(role);
        }
        return user;
    }
}