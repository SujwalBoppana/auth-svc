package com.example.auth.dto;

import lombok.Data;

@Data
public class UserAuthorityDto {
    private Long id;
    private Long userId;
    private Long authorityId;
}