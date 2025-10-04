package com.example.auth.service;

import com.example.auth.dto.AuthorityDto;
import com.example.auth.entity.Authority;
import com.example.auth.exception.ResourceNotFoundException;
import com.example.auth.repository.AuthorityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorityServiceImpl implements AuthorityService {

    private final AuthorityRepository authorityRepository;

    @Override
    public AuthorityDto createAuthority(AuthorityDto authorityDto) {
        Authority authority = toEntity(authorityDto);
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
        Authority updatedAuthority = authorityRepository.save(existingAuthority);
        return toDto(updatedAuthority);
    }

    @Override
    public void deleteAuthority(Long id) {
        if (!authorityRepository.existsById(id)) {
            throw new ResourceNotFoundException("Authority not found with id: " + id);
        }
        authorityRepository.deleteById(id);
    }

    private AuthorityDto toDto(Authority authority) {
        AuthorityDto dto = new AuthorityDto();
        dto.setId(authority.getId());
        dto.setName(authority.getName());
        dto.setCode(authority.getCode());
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