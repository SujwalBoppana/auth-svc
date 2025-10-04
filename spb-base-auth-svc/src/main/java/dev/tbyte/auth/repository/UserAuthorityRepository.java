package dev.tbyte.auth.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tbyte.auth.entity.UserAuthority;

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