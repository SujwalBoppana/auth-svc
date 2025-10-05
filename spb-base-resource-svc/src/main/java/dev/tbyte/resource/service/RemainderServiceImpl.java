package dev.tbyte.resource.service;

import dev.tbyte.resource.dto.RemainderDto;
import dev.tbyte.resource.dto.UserDto;
import dev.tbyte.resource.entity.Remainder;
import dev.tbyte.resource.exception.ResourceNotFoundException;
import dev.tbyte.resource.repository.RemainderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemainderServiceImpl implements RemainderService {

    private final RemainderRepository remainderRepository;
    private final WebClient.Builder webClientBuilder;

    @Value("${auth.service.url}")
    private String authServiceUrl;

    @Override
    public RemainderDto createRemainder(RemainderDto remainderDto, String userId) {
        Remainder remainder = toEntity(remainderDto);
        remainder.setUserId(userId); // Set the owner of the remainder
        Remainder savedRemainder = remainderRepository.save(remainder);
        return toDto(savedRemainder);
    }

    @Override
    public RemainderDto getRemainderById(Long id, String userId) {
        Remainder remainder = remainderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remainder not found with id: " + id));
        if (!remainder.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to view this remainder");
        }
        return toDto(remainder);
    }

    @Override
    public List<RemainderDto> getAllRemaindersForUser(String userId) {
        return remainderRepository.findByUserId(userId).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public RemainderDto updateRemainder(Long id, RemainderDto remainderDto, String userId) {
        Remainder existingRemainder = remainderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remainder not found with id: " + id));
        if (!existingRemainder.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to update this remainder");
        }
        existingRemainder.setTitle(remainderDto.getTitle());
        existingRemainder.setDescription(remainderDto.getDescription());
        existingRemainder.setReminderDateTime(remainderDto.getReminderDateTime());
        Remainder updatedRemainder = remainderRepository.save(existingRemainder);
        return toDto(updatedRemainder);
    }

    @Override
    public void deleteRemainder(Long id, String userId) {
        Remainder remainder = remainderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Remainder not found with id: " + id));
        if (!remainder.getUserId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this remainder");
        }
        remainderRepository.delete(remainder);
    }

    private RemainderDto toDto(Remainder remainder) {
        UserDto userDto = fetchUserFromAuthService(remainder.getUserId());
        RemainderDto remainderDto = new RemainderDto();
        remainderDto.setId(remainder.getId());
        remainderDto.setTitle(remainder.getTitle());
        remainderDto.setDescription(remainder.getDescription());
        remainderDto.setReminderDateTime(remainder.getReminderDateTime());
        remainderDto.setUser(userDto);
        return remainderDto;
    }

    private Remainder toEntity(RemainderDto dto) {
        Remainder remainder = new Remainder();
        if (dto.getId() != null) {
            remainder.setId(dto.getId());
        }
        remainder.setTitle(dto.getTitle());
        remainder.setDescription(dto.getDescription());
        remainder.setReminderDateTime(dto.getReminderDateTime());
        // userId is set from the security context, not the DTO
        return remainder;
    }

    private UserDto fetchUserFromAuthService(String userId) {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String token = jwt.getTokenValue();
        String encodedUserId = UriUtils.encode(userId, StandardCharsets.UTF_8);

        return webClientBuilder.build()
                .get()
                .uri(authServiceUrl + "/users/by-email/" + encodedUserId)
                .header("Authorization", "Bearer " + token)
                .retrieve()
                .bodyToMono(UserDto.class)
                .block();
    }
}