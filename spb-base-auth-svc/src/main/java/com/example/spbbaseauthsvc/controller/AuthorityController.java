package com.example.spbbaseauthsvc.controller;

import com.example.spbbaseauthsvc.entity.Authority;
import com.example.spbbaseauthsvc.service.AuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing authorities.
 * Access is restricted to users with the 'ADMIN' role.
 */
@RestController
@RequestMapping("/authorities")
@PreAuthorize("hasRole('ADMIN')")
public class AuthorityController {

    private final AuthorityService authorityService;

    public AuthorityController(AuthorityService authorityService) {
        this.authorityService = authorityService;
    }

    /**
     * Get all authorities.
     * @return a list of all authorities.
     */
    @GetMapping
    public List<Authority> getAllAuthorities() {
        return authorityService.getAllAuthorities();
    }

    /**
     * Get an authority by ID.
     * @param id the authority ID.
     * @return the authority.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Authority> getAuthorityById(@PathVariable Long id) {
        Authority authority = authorityService.getAuthorityById(id);
        return ResponseEntity.ok(authority);
    }

    /**
     * Create a new authority.
     * @param authority the authority to create.
     * @return the created authority.
     */
    @PostMapping
    public ResponseEntity<Authority> createAuthority(@RequestBody Authority authority) {
        Authority createdAuthority = authorityService.createAuthority(authority);
        return ResponseEntity.ok(createdAuthority);
    }

    /**
     * Update an existing authority.
     * @param id the ID of the authority to update.
     * @param authorityDetails the new authority details.
     * @return the updated authority.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Authority> updateAuthority(@PathVariable Long id, @RequestBody Authority authorityDetails) {
        Authority updatedAuthority = authorityService.updateAuthority(id, authorityDetails);
        return ResponseEntity.ok(updatedAuthority);
    }

    /**
     * Delete an authority.
     * @param id the ID of the authority to delete.
     * @return a no-content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.deleteAuthority(id);
        return ResponseEntity.noContent().build();
    }
}