package com.pcb.audy.domain.pin.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinOrderUpdateRes {
    private UUID pinId;
    private int order;

    @Builder
    private PinOrderUpdateRes(UUID pinId, int order) {
        this.pinId = pinId;
        this.order = order;
    }
}
