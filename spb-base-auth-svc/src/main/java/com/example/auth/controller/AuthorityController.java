package com.example.auth.controller;

import com.example.auth.dto.AuthorityDto;
import com.example.auth.service.AuthorityService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authorities")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class AuthorityController {

    private final AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<AuthorityDto> createAuthority(@RequestBody AuthorityDto authorityDto) {
        return new ResponseEntity<>(authorityService.createAuthority(authorityDto), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorityDto> getAuthorityById(@PathVariable Long id) {
        return ResponseEntity.ok(authorityService.getAuthorityById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        return ResponseEntity.ok(authorityService.getAllAuthorities());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorityDto> updateAuthority(@PathVariable Long id, @RequestBody AuthorityDto authorityDto) {
        return ResponseEntity.ok(authorityService.updateAuthority(id, authorityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthority(@PathVariable Long id) {
        authorityService.deleteAuthority(id);
        return ResponseEntity.noContent().build();
    }
}