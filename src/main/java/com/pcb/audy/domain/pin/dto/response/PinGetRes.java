package com.pcb.audy.domain.pin.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinGetRes {
    private UUID pinId;
    private String pinName;
    private String originName;
    private Double latitude;
    private Double longitude;
    private String address;
    private Integer sequence;

    @Builder
    private PinGetRes(
            UUID pinId,
            String pinName,
            String originName,
            Double latitude,
            Double longitude,
            String address,
            Integer sequence) {
        this.pinId = pinId;
        this.pinName = pinName;
        this.originName = originName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sequence = sequence;
    }
}
