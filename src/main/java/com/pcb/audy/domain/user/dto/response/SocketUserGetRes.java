package com.pcb.audy.domain.user.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketUserGetRes {
    private Long userId;

    @Builder
    private SocketUserGetRes(Long userId) {
        this.userId = userId;
    }
}
