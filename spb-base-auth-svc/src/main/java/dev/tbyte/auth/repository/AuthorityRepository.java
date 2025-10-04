package dev.tbyte.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import dev.tbyte.auth.entity.Authority;

/**
 * JPA Repository for the Authority entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}