package dev.tbyte.auth.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dev.tbyte.auth.entity.User;
import dev.tbyte.auth.entity.UserAuthority;
import dev.tbyte.auth.repository.UserAuthorityRepository;
import dev.tbyte.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;

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

        return new CustomUserDetails(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                grantedAuthorities);
    }
}