package dev.tbyte.auth.service;

import java.util.List;

import dev.tbyte.auth.dto.UserAuthorityDto;

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