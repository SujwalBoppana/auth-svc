package dev.tbyte.resource.service;

import java.util.List;

import dev.tbyte.resource.dto.RemainderDto;

/**
 * Service interface for managing Remainders.
 */
public interface RemainderService {
    RemainderDto createRemainder(RemainderDto remainderDto, String userId);

    RemainderDto getRemainderById(Long id, String userId);

    List<RemainderDto> getAllRemaindersForUser(String userId);

    RemainderDto updateRemainder(Long id, RemainderDto remainderDto, String userId);

    void deleteRemainder(Long id, String userId);
}