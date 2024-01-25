package com.pcb.audy.global.jwt.tokens;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {
    private String token;
    private Long expireTime;

    @Builder
    private AccessToken(String token, Long expireTime) {
        this.token = token;
        this.expireTime = expireTime;
    }
}
