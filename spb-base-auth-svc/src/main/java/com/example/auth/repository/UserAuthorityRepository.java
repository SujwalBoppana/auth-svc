package com.example.auth.repository;

import com.example.auth.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the UserAuthority entity.
 */
@Repository
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {

    /**
     * Finds all authorities associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A list of UserAuthority objects.
     */
    List<UserAuthority> findByUserId(Long userId);
}