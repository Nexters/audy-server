package com.pcb.audy.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGetRes {
    private Long userId;
    private String email;
    private String username;
    private String imageUrl;

    @Builder
    private UserGetRes(Long userId, String email, String username, String imageUrl) {
        this.userId = userId;
        this.email = email;
        this.username = username;
        this.imageUrl = imageUrl;
    }
}
