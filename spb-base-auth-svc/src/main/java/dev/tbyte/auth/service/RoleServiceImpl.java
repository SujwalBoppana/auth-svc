package dev.tbyte.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.tbyte.auth.dto.RoleDto;
import dev.tbyte.auth.entity.Role;
import dev.tbyte.auth.exception.ResourceNotFoundException;
import dev.tbyte.auth.repository.RoleRepository;
import dev.tbyte.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public RoleDto createRole(RoleDto roleDto) {
        Role role = toEntity(roleDto);
        Long currentUserId = SecurityUtil.getCurrentUserId();
        role.setCreatedBy(currentUserId);
        role.setUpdatedBy(currentUserId);
        Role savedRole = roleRepository.save(role);
        return toDto(savedRole);
    }

    @Override
    public RoleDto getRoleById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        return toDto(role);
    }

    @Override
    public List<RoleDto> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public RoleDto updateRole(Long id, RoleDto roleDto) {
        Role existingRole = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        existingRole.setName(roleDto.getName());
        existingRole.setCode(roleDto.getCode());
        existingRole.setUpdatedBy(SecurityUtil.getCurrentUserId());
        Role updatedRole = roleRepository.save(existingRole);
        return toDto(updatedRole);
    }

    @Override
    public void deleteRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
        role.setDeleted(true);
        role.setUpdatedBy(SecurityUtil.getCurrentUserId());
        roleRepository.save(role);
    }

    private RoleDto toDto(Role role) {
        RoleDto dto = new RoleDto();
        dto.setId(role.getId());
        dto.setName(role.getName());
        dto.setCode(role.getCode());
        dto.setCreatedAt(role.getCreatedAt());
        dto.setUpdatedAt(role.getUpdatedAt());
        dto.setCreatedBy(role.getCreatedBy());
        dto.setUpdatedBy(role.getUpdatedBy());
        dto.setDeleted(role.isDeleted());
        return dto;
    }

    private Role toEntity(RoleDto dto) {
        Role role = new Role();
        role.setId(dto.getId());
        role.setName(dto.getName());
        role.setCode(dto.getCode());
        return role;
    }
}