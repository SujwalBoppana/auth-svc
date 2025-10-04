package dev.tbyte.auth.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dev.tbyte.auth.dto.UserAuthorityDto;
import dev.tbyte.auth.service.UserAuthorityService;

import java.util.List;

@RestController
@RequestMapping("/user-authorities")
@RequiredArgsConstructor
@SecurityRequirement(name = "BearerAuth")
@PreAuthorize("hasRole('ADMIN')")
public class UserAuthorityController {

    private final UserAuthorityService userAuthorityService;

    @PostMapping
    public ResponseEntity<UserAuthorityDto> grantAuthority(@RequestBody UserAuthorityDto userAuthorityDto) {
        return new ResponseEntity<>(userAuthorityService.grantAuthorityToUser(userAuthorityDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserAuthorityDto>> getAllUserAuthorities() {
        return ResponseEntity.ok(userAuthorityService.getAllUserAuthorities());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserAuthorityDto> getUserAuthorityById(@PathVariable Long id) {
        return ResponseEntity.ok(userAuthorityService.getUserAuthorityById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UserAuthorityDto>> getAuthoritiesForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(userAuthorityService.getAuthoritiesForUser(userId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserAuthorityDto> updateUserAuthority(@PathVariable Long id,
            @RequestBody UserAuthorityDto userAuthorityDto) {
        return ResponseEntity.ok(userAuthorityService.updateUserAuthority(id, userAuthorityDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> revokeAuthority(@PathVariable Long id) {
        userAuthorityService.revokeAuthorityFromUser(id);
        return ResponseEntity.noContent().build();
    }
}