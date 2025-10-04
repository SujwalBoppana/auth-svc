package com.example.resource.repository;

import com.example.resource.entity.Remainder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JPA Repository for the Remainder entity.
 */
@Repository
public interface RemainderRepository extends JpaRepository<Remainder, Long> {

    /**
     * Finds all remainders for a specific user.
     *
     * @param userId The identifier of the user.
     * @return A list of remainders.
     */
    List<Remainder> findByUserId(String userId);
}