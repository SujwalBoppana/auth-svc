package com.example.spbbaseauthsvc.service;

import com.example.spbbaseauthsvc.entity.User;
import com.example.spbbaseauthsvc.repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Service to load user-specific data for Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        // Add role as a granted authority, prefixed with "ROLE_"
        if (user.getRole() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode()));
        }

        // Add other authorities
        if (user.getAuthorities() != null) {
            user.getAuthorities().forEach(userAuthority ->
                    grantedAuthorities.add(new SimpleGrantedAuthority(userAuthority.getAuthority().getCode()))
            );
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}