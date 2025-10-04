package com.example.auth.repository;

import com.example.auth.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA Repository for the Authority entity.
 */
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}