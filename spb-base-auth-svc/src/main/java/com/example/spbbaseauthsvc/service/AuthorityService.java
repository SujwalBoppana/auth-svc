package com.example.spbbaseauthsvc.service;

import com.example.spbbaseauthsvc.entity.Authority;
import com.example.spbbaseauthsvc.repository.AuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing authorities.
 */
@Service
public class AuthorityService {

    private final AuthorityRepository authorityRepository;

    public AuthorityService(AuthorityRepository authorityRepository) {
        this.authorityRepository = authorityRepository;
    }

    @Transactional(readOnly = true)
    public List<Authority> getAllAuthorities() {
        return authorityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Authority getAuthorityById(Long id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Authority not found with id: " + id));
    }

    @Transactional
    public Authority createAuthority(Authority authority) {
        return authorityRepository.save(authority);
    }

    @Transactional
    public Authority updateAuthority(Long id, Authority authorityDetails) {
        Authority authority = getAuthorityById(id);
        authority.setName(authorityDetails.getName());
        authority.setCode(authorityDetails.getCode());
        return authorityRepository.save(authority);
    }

    @Transactional
    public void deleteAuthority(Long id) {
        Authority authority = getAuthorityById(id);
        authorityRepository.delete(authority);
    }
}