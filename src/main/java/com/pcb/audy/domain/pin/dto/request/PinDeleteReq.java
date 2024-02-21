package com.pcb.audy.domain.pin.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinDeleteReq {
    private UUID pinId;

    @Builder
    private PinDeleteReq(UUID pinId) {
        this.pinId = pinId;
    }
}
