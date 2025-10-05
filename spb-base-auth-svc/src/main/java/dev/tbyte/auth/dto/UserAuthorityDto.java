package dev.tbyte.auth.dto;

import lombok.Data;

@Data
public class UserAuthorityDto {
    private Long id;
    private String userName;
    private String authorityName;
}