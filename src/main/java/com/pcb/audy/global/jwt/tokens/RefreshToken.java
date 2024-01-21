package com.pcb.audy.global.jwt.tokens;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    private String token;
    private Long expireTime;

    @Builder
    private RefreshToken(String token, Long expireTime) {
        this.token = token;
        this.expireTime = expireTime;
    }
}
