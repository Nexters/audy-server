package com.pcb.audy.domain.user.entity;

import com.pcb.audy.domain.model.BaseEntity;
import com.pcb.audy.global.meta.Authority;
import com.pcb.audy.global.meta.Social;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "tb_user")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String oauthId;
    private String email;
    private String username;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Enumerated(EnumType.STRING)
    private Social social;

    private String imageUrl;

    @Builder
    private User(
            Long userId,
            String oauthId,
            String email,
            String username,
            Authority authority,
            Social social,
            String imageUrl) {
        this.userId = userId;
        this.oauthId = oauthId;
        this.email = email;
        this.username = username;
        this.authority = authority;
        this.social = social;
        this.imageUrl = imageUrl;
    }
}
