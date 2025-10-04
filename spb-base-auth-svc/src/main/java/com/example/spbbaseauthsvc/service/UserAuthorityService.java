package com.example.spbbaseauthsvc.service;

import com.example.spbbaseauthsvc.entity.UserAuthority;
import com.example.spbbaseauthsvc.repository.UserAuthorityRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service for managing user-authority relationships.
 */
@Service
public class UserAuthorityService {

    private final UserAuthorityRepository userAuthorityRepository;

    public UserAuthorityService(UserAuthorityRepository userAuthorityRepository) {
        this.userAuthorityRepository = userAuthorityRepository;
    }

    @Transactional(readOnly = true)
    public List<UserAuthority> getAllUserAuthorities() {
        return userAuthorityRepository.findAll();
    }

    @Transactional(readOnly = true)
    public UserAuthority getUserAuthorityById(Long id) {
        return userAuthorityRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("UserAuthority not found with id: " + id));
    }

    @Transactional
    public UserAuthority createUserAuthority(UserAuthority userAuthority) {
        // In a real app, you'd validate that the user and authority exist.
        return userAuthorityRepository.save(userAuthority);
    }

    @Transactional
    public void deleteUserAuthority(Long id) {
        UserAuthority userAuthority = getUserAuthorityById(id);
        userAuthorityRepository.delete(userAuthority);
    }
}