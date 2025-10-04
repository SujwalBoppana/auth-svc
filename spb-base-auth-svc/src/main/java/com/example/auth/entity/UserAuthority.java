package com.example.auth.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Maps a User to a specific Authority, granting them that permission.
 */
@Entity
@Table(name = "user_authorities", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "authority_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "authority_id", nullable = false)
    private Authority authority;
}