package dev.tbyte.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.tbyte.auth.dto.UserAuthorityDto;
import dev.tbyte.auth.entity.Authority;
import dev.tbyte.auth.entity.User;
import dev.tbyte.auth.entity.UserAuthority;
import dev.tbyte.auth.exception.ResourceNotFoundException;
import dev.tbyte.auth.repository.AuthorityRepository;
import dev.tbyte.auth.repository.UserAuthorityRepository;
import dev.tbyte.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserAuthorityServiceImpl implements UserAuthorityService {

    private final UserAuthorityRepository userAuthorityRepository;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    @Override
    public UserAuthorityDto grantAuthorityToUser(UserAuthorityDto userAuthorityDto) {
        User user = userRepository.findByEmail(userAuthorityDto.getUserName())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with email: " + userAuthorityDto.getUserName()));
        Authority authority = authorityRepository.findByName(userAuthorityDto.getAuthorityName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Authority not found with name: " + userAuthorityDto.getAuthorityName()));

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setUser(user);
        userAuthority.setAuthority(authority);

        UserAuthority savedUserAuthority = userAuthorityRepository.save(userAuthority);
        return toDto(savedUserAuthority);
    }

    @Override
    public void revokeAuthorityFromUser(Long userAuthorityId) {
        if (!userAuthorityRepository.existsById(userAuthorityId)) {
            throw new ResourceNotFoundException("UserAuthority mapping not found with id: " + userAuthorityId);
        }
        userAuthorityRepository.deleteById(userAuthorityId);
    }

    @Override
    public List<UserAuthorityDto> getAuthoritiesForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return userAuthorityRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<UserAuthorityDto> getAllUserAuthorities() {
        return userAuthorityRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public UserAuthorityDto getUserAuthorityById(Long id) {
        UserAuthority userAuthority = userAuthorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAuthority mapping not found with id: " + id));
        return toDto(userAuthority);
    }

    @Override
    public UserAuthorityDto updateUserAuthority(Long id, UserAuthorityDto userAuthorityDto) {
        UserAuthority existingMapping = userAuthorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("UserAuthority mapping not found with id: " + id));

        User user = userRepository.findByEmail(userAuthorityDto.getUserName())
                .orElseThrow(
                        () -> new ResourceNotFoundException("User not found with email: " + userAuthorityDto.getUserName()));
        Authority authority = authorityRepository.findByName(userAuthorityDto.getAuthorityName())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Authority not found with name: " + userAuthorityDto.getAuthorityName()));

        existingMapping.setUser(user);
        existingMapping.setAuthority(authority);

        UserAuthority updatedMapping = userAuthorityRepository.save(existingMapping);
        return toDto(updatedMapping);
    }

    private UserAuthorityDto toDto(UserAuthority userAuthority) {
        UserAuthorityDto dto = new UserAuthorityDto();
        dto.setId(userAuthority.getId());
        dto.setUserName(userAuthority.getUser().getEmail());
        dto.setAuthorityName(userAuthority.getAuthority().getName());
        return dto;
    }
}