package dev.tbyte.auth.dto;

import lombok.Data;

@Data
public class UserAuthorityDto {
    private Long pk_id;
    private String userEmail;
    private String authorityCode;
}