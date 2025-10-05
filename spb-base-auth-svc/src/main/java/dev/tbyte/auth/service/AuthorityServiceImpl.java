package dev.tbyte.auth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import dev.tbyte.auth.dto.AuthorityDto;
import dev.tbyte.auth.entity.Authority;
import dev.tbyte.auth.exception.ResourceNotFoundException;
import dev.tbyte.auth.repository.AuthorityRepository;
import dev.tbyte.auth.util.SecurityUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public AuthorityDto createAuthority(AuthorityDto authorityDto) {
        Authority authority = toEntity(authorityDto);
        Long currentUserId = SecurityUtil.getCurrentUserId();
        authority.setCreatedBy(currentUserId);
        authority.setUpdatedBy(currentUserId);
        Authority savedAuthority = authorityRepository.save(authority);
        return toDto(savedAuthority);
    }

    @Override
    public AuthorityDto getAuthorityById(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Authority not found with id: " + id));
        return toDto(authority);
    }

    @Override
    public List<AuthorityDto> getAllAuthorities() {
        return authorityRepository.findAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @Override
    public AuthorityDto updateAuthority(Long id, AuthorityDto authorityDto) {
        Authority existingAuthority = authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Authority not found with id: " + id));
        existingAuthority.setName(authorityDto.getName());
        existingAuthority.setCode(authorityDto.getCode());
        existingAuthority.setUpdatedBy(SecurityUtil.getCurrentUserId());
        Authority updatedAuthority = authorityRepository.save(existingAuthority);
        return toDto(updatedAuthority);
    }

    @Override
    public void deleteAuthority(Long id) {
        Authority authority = authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Authority not found with id: " + id));
        authority.setDeleted(true);
        authority.setUpdatedBy(SecurityUtil.getCurrentUserId());
        authorityRepository.save(authority);
    }

    private AuthorityDto toDto(Authority authority) {
        AuthorityDto dto = new AuthorityDto();
        dto.setId(authority.getId());
        dto.setName(authority.getName());
        dto.setCode(authority.getCode());
        dto.setCreatedAt(authority.getCreatedAt());
        dto.setUpdatedAt(authority.getUpdatedAt());
        dto.setCreatedBy(authority.getCreatedBy());
        dto.setUpdatedBy(authority.getUpdatedBy());
        dto.setDeleted(authority.isDeleted());
        return dto;
    }

    private Authority toEntity(AuthorityDto dto) {
        Authority authority = new Authority();
        authority.setId(dto.getId());
        authority.setName(dto.getName());
        authority.setCode(dto.getCode());
        return authority;
    }
}