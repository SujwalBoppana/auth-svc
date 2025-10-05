package dev.tbyte.auth.util;

import dev.tbyte.auth.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class SecurityUtil {

    private static final Long GUEST_USER_ID = 3L;

    public static Long getCurrentUserId() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
                .map(Authentication::getPrincipal)
                .filter(principal -> !principal.equals("anonymousUser"))
                .map(principal -> (CustomUserDetails) principal)
                .map(CustomUserDetails::getId)
                .orElse(GUEST_USER_ID);
    }
}