package dev.tbyte.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tbyte.auth.entity.Role;

/**
 * JPA Repository for the Role entity.
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    /**
     * Finds a role by its unique code.
     *
     * @param code The role code to search for.
     * @return An Optional containing the role if found.
     */
    Optional<Role> findByCode(String code);
}