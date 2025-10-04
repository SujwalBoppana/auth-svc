package com.example.auth.service;

import com.example.auth.dto.RoleDto;

import java.util.List;

/**
 * Service interface for managing Roles.
 */
public interface RoleService {
    RoleDto createRole(RoleDto roleDto);
    RoleDto getRoleById(Long id);
    List<RoleDto> getAllRoles();
    RoleDto updateRole(Long id, RoleDto roleDto);
    void deleteRole(Long id);
}