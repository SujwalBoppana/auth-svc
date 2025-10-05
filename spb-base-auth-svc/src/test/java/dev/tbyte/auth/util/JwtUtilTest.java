package dev.tbyte.auth.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secret", "testsecretkey-is-long-enough-for-hs256-encryption");
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 3600000L);
        ReflectionTestUtils.setField(jwtUtil, "refreshExpirationMs", 7200000L);
        userDetails = new User("testuser", "password", new ArrayList<>());
    }

    @Test
    void whenGenerateToken_thenCorrectUsernameIsEncoded() {
        String token = jwtUtil.generateToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    void whenGenerateRefreshToken_thenCorrectUsernameIsEncoded() {
        String token = jwtUtil.generateRefreshToken(userDetails);
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertEquals("testuser", jwtUtil.extractUsername(token));
    }

    @Test
    void whenExtractExpiration_thenCorrectDateIsReturned() {
        long expirationMs = 3600000L;
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", expirationMs);
        String token = jwtUtil.generateToken(userDetails);
        Date expiration = jwtUtil.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    void whenValidateTokenWithValidToken_thenReturnsTrue() {
        String token = jwtUtil.generateToken(userDetails);
        assertTrue(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void whenValidateTokenWithDifferentUser_thenReturnsFalse() {
        String token = jwtUtil.generateToken(userDetails);
        UserDetails otherUserDetails = new User("otheruser", "password", new ArrayList<>());
        assertFalse(jwtUtil.validateToken(token, otherUserDetails));
    }

    @Test
    void whenValidateTokenWithExpiredToken_thenReturnsFalse() throws InterruptedException {
        ReflectionTestUtils.setField(jwtUtil, "expirationMs", 1L);
        String token = jwtUtil.generateToken(userDetails);
        Thread.sleep(10); // Wait for token to expire
        assertFalse(jwtUtil.validateToken(token, userDetails));
    }

    @Test
    void whenValidateTokenWithInvalidToken_thenThrowsException() {
        assertThrows(Exception.class, () -> {
            jwtUtil.validateToken("invalid-token", userDetails);
        });
    }

    @Test
    void whenValidateTokenWithWrongSecret_thenThrowsException() {
        String token = jwtUtil.generateToken(userDetails);

        // Create another JwtUtil with a different secret
        JwtUtil otherJwtUtil = new JwtUtil();
        ReflectionTestUtils.setField(otherJwtUtil, "secret", "another-different-secret-key-that-is-also-long-enough");
        ReflectionTestUtils.setField(otherJwtUtil, "expirationMs", 3600000L);

        assertThrows(Exception.class, () -> {
            otherJwtUtil.validateToken(token, userDetails);
        });
    }
}