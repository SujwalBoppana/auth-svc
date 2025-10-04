package com.example.auth.service;

import com.example.auth.entity.User;
import com.example.auth.entity.UserAuthority;
import com.example.auth.repository.UserAuthorityRepository;
import com.example.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Custom implementation of UserDetailsService to load user-specific data.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserAuthorityRepository userAuthorityRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();

        // Add the user's role as a granted authority, prefixed with "ROLE_"
        if (user.getRole() != null) {
            grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getCode()));
        }

        // Add all specific authorities assigned to the user
        List<UserAuthority> userAuthorities = userAuthorityRepository.findByUserId(user.getId());
        userAuthorities.forEach(ua -> grantedAuthorities.add(new SimpleGrantedAuthority(ua.getAuthority().getCode())));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities
        );
    }
}