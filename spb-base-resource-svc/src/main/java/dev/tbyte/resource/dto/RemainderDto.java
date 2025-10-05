package dev.tbyte.resource.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RemainderDto {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime reminderDateTime;
    private UserDto user;
}