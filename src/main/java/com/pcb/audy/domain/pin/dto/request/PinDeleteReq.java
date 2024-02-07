package com.pcb.audy.domain.pin.dto.request;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinDeleteReq {
    private Long courseId;
    private UUID pinId;

    @Builder
    private PinDeleteReq(Long courseId, UUID pinId) {
        this.courseId = courseId;
        this.pinId = pinId;
    }
}
