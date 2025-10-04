package com.example.auth.service;

import com.example.auth.dto.AuthorityDto;

import java.util.List;

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