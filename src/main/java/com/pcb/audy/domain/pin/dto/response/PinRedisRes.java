package com.pcb.audy.domain.pin.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinRedisRes {
    private Long courseId;
    private UUID pinId;
    private String pinName;
    private String originName;
    private Double latitude;
    private Double longitude;
    private String address;
    private Integer sequence;

    @Builder
    private PinRedisRes(
            Long courseId,
            UUID pinId,
            String pinName,
            String originName,
            Double latitude,
            Double longitude,
            String address,
            Integer sequence) {
        this.courseId = courseId;
        this.pinId = pinId;
        this.pinName = pinName;
        this.originName = originName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sequence = sequence;
    }
}
