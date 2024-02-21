package com.pcb.audy.domain.pin.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinNameUpdateReq {
    private UUID pinId;
    private String pinName;

    @Builder
    private PinNameUpdateReq(UUID pinId, String pinName) {
        this.pinId = pinId;
        this.pinName = pinName;
    }
}
