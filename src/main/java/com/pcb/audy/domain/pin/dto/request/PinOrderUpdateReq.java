package com.pcb.audy.domain.pin.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinOrderUpdateReq {
    private UUID pinId;
    private int order;

    @Builder
    private PinOrderUpdateReq(UUID pinId, int order) {
        this.pinId = pinId;
        this.order = order;
    }
}
