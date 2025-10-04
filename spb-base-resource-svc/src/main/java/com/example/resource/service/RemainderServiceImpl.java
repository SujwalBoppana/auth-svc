package com.example.resource.service;

import com.example.resource.dto.RemainderDto;
import com.example.resource.entity.Remainder;
import com.example.resource.exception.ResourceNotFoundException;
import com.example.resource.repository.RemainderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RemainderServiceImpl implements RemainderService {

    private final RemainderRepository remainderRepository;

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
        RemainderDto dto = new RemainderDto();
        dto.setId(remainder.getId());
        dto.setTitle(remainder.getTitle());
        dto.setDescription(remainder.getDescription());
        dto.setReminderDateTime(remainder.getReminderDateTime());
        dto.setUserId(remainder.getUserId());
        dto.setCreatedAt(remainder.getCreatedAt());
        dto.setUpdatedAt(remainder.getUpdatedAt());
        return dto;
    }

    private Remainder toEntity(RemainderDto dto) {
        Remainder remainder = new Remainder();
        remainder.setId(dto.getId());
        remainder.setTitle(dto.getTitle());
        remainder.setDescription(dto.getDescription());
        remainder.setReminderDateTime(dto.getReminderDateTime());
        // userId is set from the security context, not the DTO
        return remainder;
    }
}