package com.pcb.audy.domain.pin.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinDeleteRes {
    private UUID pinId;

    @Builder
    private PinDeleteRes(UUID pinId) {
        this.pinId = pinId;
    }
}
