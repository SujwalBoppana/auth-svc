package dev.tbyte.auth.dto;

import lombok.Data;

@Data
public class AuthorityDto {
    private Long id;
    private String name;
    private String code;
}