package dev.tbyte.auth.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RoleDto {
    private Long id;
    private String name;
    private String code;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long createdBy;
    private Long updatedBy;
    private boolean isDeleted;
}