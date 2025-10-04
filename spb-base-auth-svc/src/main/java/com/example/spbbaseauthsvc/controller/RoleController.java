package com.example.spbbaseauthsvc.controller;

import com.example.spbbaseauthsvc.entity.Role;
import com.example.spbbaseauthsvc.service.RoleService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for managing roles.
 * Access is restricted to users with the 'ADMIN' role.
 */
@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ADMIN')")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * Get all roles.
     * @return a list of all roles.
     */
    @GetMapping
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    /**
     * Get a role by ID.
     * @param id the role ID.
     * @return the role.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Role> getRoleById(@PathVariable Long id) {
        Role role = roleService.getRoleById(id);
        return ResponseEntity.ok(role);
    }

    /**
     * Create a new role.
     * @param role the role to create.
     * @return the created role.
     */
    @PostMapping
    public ResponseEntity<Role> createRole(@RequestBody Role role) {
        Role createdRole = roleService.createRole(role);
        return ResponseEntity.ok(createdRole);
    }

    /**
     * Update an existing role.
     * @param id the ID of the role to update.
     * @param roleDetails the new role details.
     * @return the updated role.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable Long id, @RequestBody Role roleDetails) {
        Role updatedRole = roleService.updateRole(id, roleDetails);
        return ResponseEntity.ok(updatedRole);
    }

    /**
     * Delete a role.
     * @param id the ID of the role to delete.
     * @return a no-content response.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }
}