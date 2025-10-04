package dev.tbyte.auth.service;

import java.util.List;

import dev.tbyte.auth.dto.AuthorityDto;

/**
 * Service interface for managing Authorities.
 */
public interface AuthorityService {
    AuthorityDto createAuthority(AuthorityDto authorityDto);

    AuthorityDto getAuthorityById(Long id);

    List<AuthorityDto> getAllAuthorities();

    AuthorityDto updateAuthority(Long id, AuthorityDto authorityDto);

    void deleteAuthority(Long id);
}