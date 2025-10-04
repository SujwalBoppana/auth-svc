package com.example.spbbaseauthsvc.controller;

import com.example.spbbaseauthsvc.entity.UserAuthority;
import com.example.spbbaseauthsvc.service.UserAuthorityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing the assignment of authorities to users.
 * Access is restricted to users with the 'ADMIN' role.
 */
@RestController
@RequestMapping("/user-authorities")
@PreAuthorize("hasRole('ADMIN')")
public class UserAuthorityController {

    private final UserAuthorityService userAuthorityService;

    public UserAuthorityController(UserAuthorityService userAuthorityService) {
        this.userAuthorityService = userAuthorityService;
    }

    /**
     * Get all user-authority assignments.
     * @return a list of all assignments.
     */
    @GetMapping
    public List<UserAuthority> getAllUserAuthorities() {
        return userAuthorityService.getAllUserAuthorities();
    }

    /**
     * Get a user-authority assignment by ID.
     * @param id the assignment ID.
     * @return the assignment.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserAuthority> getUserAuthorityById(@PathVariable Long id) {
        UserAuthority userAuthority = userAuthorityService.getUserAuthorityById(id);
        return ResponseEntity.ok(userAuthority);
    }

    /**
     * Assign an authority to a user.
     * @param userAuthority the assignment to create.
     * @return the created assignment.
     */
    @PostMapping
    public ResponseEntity<UserAuthority> createUserAuthority(@RequestBody UserAuthority userAuthority) {
        // In a real application, you would likely pass User ID and Authority ID
        // and construct the UserAuthority object in the service layer after validation.
        UserAuthority createdUserAuthority = userAuthorityService.createUserAuthority(userAuthority);
        return ResponseEntity.ok(createdUserAuthority);
    }

    /**
     * Delete a user-authority assignment.
     * @param id the ID of the assignment to delete.
     * @return a no-content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserAuthority(@PathVariable Long id) {
        userAuthorityService.deleteUserAuthority(id);
        return ResponseEntity.noContent().build();
    }
}