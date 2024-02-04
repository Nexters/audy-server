package com.pcb.audy.global.oauth.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OAuth2Res {
    private Long userId;
    private String email;
    private String username;
    private String imageUrl;

    @Builder
    private OAuth2Res(Long userId, String email, String username, String imageUrl) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}
