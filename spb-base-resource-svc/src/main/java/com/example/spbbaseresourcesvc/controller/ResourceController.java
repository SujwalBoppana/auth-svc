package com.example.spbbaseresourcesvc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;

/**
 * A sample controller to demonstrate protected resources.
 */
@RestController
@RequestMapping("/api/v1/resources")
public class ResourceController {

    /**
     * A public endpoint, accessible to any authenticated user with the 'USER' role.
     *
     * @param principal The authenticated user.
     * @return A welcome message.
     */
    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Map<String, String> userResource(Principal principal) {
        return Map.of("message", "Welcome, " + principal.getName() + "! This is a USER-level resource.");
    }

    /**
     * An admin-only endpoint.
     *
     * @param principal The authenticated user.
     * @return A welcome message for admins.
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> adminResource(Principal principal) {
        return Map.of("message", "Welcome, " + principal.getName() + "! This is an ADMIN-level resource.");
    }

    /**
     * An endpoint requiring a specific authority, e.g., 'READ_PRIVILEGE'.
     *
     * @param principal The authenticated user.
     * @return A message indicating access to a specific resource.
     */
    @GetMapping("/view")
    @PreAuthorize("hasAuthority('READ_PRIVILEGE')")
    public Map<String, String> viewResource(Principal principal) {
        return Map.of("message", "Welcome, " + principal.getName() + "! You have the 'READ_PRIVILEGE' authority.");
    }

     /**
     * An endpoint requiring a specific authority, e.g., 'WRITE_PRIVILEGE'.
     *
     * @param principal The authenticated user.
     * @return A message indicating access to a specific resource.
     */
    @GetMapping("/edit")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public Map<String, String> editResource(Principal principal) {
        return Map.of("message", "Welcome, " + principal.getName() + "! You have the 'WRITE_PRIVILEGE' authority.");
    }
}