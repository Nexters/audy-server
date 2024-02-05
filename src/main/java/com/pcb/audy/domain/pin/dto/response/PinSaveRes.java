package com.pcb.audy.domain.pin.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinSaveRes {
    private String pinId;

    @Builder
    private PinSaveRes(String pinId) {
        this.pinId = pinId;
    }
}
