package dev.tbyte.auth.service;

import java.util.List;

import dev.tbyte.auth.dto.RoleDto;

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