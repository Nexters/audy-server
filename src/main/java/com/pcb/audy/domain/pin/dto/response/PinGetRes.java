package com.pcb.audy.domain.pin.dto.response;

import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinGetRes {
    private UUID pinId;
    private String pinName;
    private String originName;
    private Double latitude;
    private Double longitude;
    private String address;
    private String sequence;

    @Builder
    private PinGetRes(
            UUID pinId,
            String pinName,
            String originName,
            Double latitude,
            Double longitude,
            String address,
            String sequence) {
        this.pinId = pinId;
        this.pinName = pinName;
        this.originName = originName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sequence = sequence;
    }
}
