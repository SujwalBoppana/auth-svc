package com.example.auth.service;

import com.example.auth.dto.UserAuthorityDto;

import java.util.List;

import java.util.List;

/**
 * Service interface for managing UserAuthorities.
 */
public interface UserAuthorityService {
    UserAuthorityDto grantAuthorityToUser(UserAuthorityDto userAuthorityDto);
    void revokeAuthorityFromUser(Long userAuthorityId);
    List<UserAuthorityDto> getAuthoritiesForUser(Long userId);
    List<UserAuthorityDto> getAllUserAuthorities();
    UserAuthorityDto getUserAuthorityById(Long id);
    UserAuthorityDto updateUserAuthority(Long id, UserAuthorityDto userAuthorityDto);
}