package dev.tbyte.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import dev.tbyte.auth.entity.Authority;

import java.util.List;
import java.util.Optional;

/**
 * JPA Repository for the Authority entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Optional<Authority> findByName(String name);
    Optional<Authority> findByCode(String code);

    /**
     * Finds all authorities, including those that are soft-deleted.
     *
     * @return A list of all authorities.
     */
    @Query(value = "SELECT * FROM authorities", nativeQuery = true)
    List<Authority> findAllIncludingDeleted();
}