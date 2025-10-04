package dev.tbyte.resource.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RemainderDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime reminderDateTime;
    private String userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}