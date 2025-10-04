package com.example.resource.controller;

import com.example.resource.dto.RemainderDto;
import com.example.resource.service.RemainderService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/remainders")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
public class RemainderController {

    private final RemainderService remainderService;

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_REMAINDER')")
    public ResponseEntity<RemainderDto> createRemainder(@RequestBody RemainderDto remainderDto, Authentication authentication) {
        String userId = authentication.getName();
        return new ResponseEntity<>(remainderService.createRemainder(remainderDto, userId), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('READ_REMAINDER')")
    public ResponseEntity<RemainderDto> getRemainderById(@PathVariable Long id, Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(remainderService.getRemainderById(id, userId));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_REMAINDER')")
    public ResponseEntity<List<RemainderDto>> getAllRemaindersForUser(Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(remainderService.getAllRemaindersForUser(userId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('UPDATE_REMAINDER')")
    public ResponseEntity<RemainderDto> updateRemainder(@PathVariable Long id, @RequestBody RemainderDto remainderDto, Authentication authentication) {
        String userId = authentication.getName();
        return ResponseEntity.ok(remainderService.updateRemainder(id, remainderDto, userId));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('DELETE_REMAINDER')")
    public ResponseEntity<Void> deleteRemainder(@PathVariable Long id, Authentication authentication) {
        String userId = authentication.getName();
        remainderService.deleteRemainder(id, userId);
        return ResponseEntity.noContent().build();
    }
}