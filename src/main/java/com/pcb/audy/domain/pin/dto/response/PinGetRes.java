package com.pcb.audy.domain.pin.dto.response;

import com.github.pravin.raha.lexorank4j.LexoRank;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinGetRes implements Comparable<PinGetRes> {
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

    @Override
    public int compareTo(PinGetRes other) {
        LexoRank thisRank = LexoRank.parse(this.sequence);
        LexoRank otherRank = LexoRank.parse(other.sequence);
        return thisRank.compareTo(otherRank);
    }
}
