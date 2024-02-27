package com.pcb.audy.domain.pin.dto.response;

import com.github.pravin.raha.lexorank4j.LexoRank;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PinRedisRes implements Comparable<PinRedisRes> {
    private Long courseId;
    private UUID pinId;
    private String pinName;
    private String originName;
    private Double latitude;
    private Double longitude;
    private String address;
    private String sequence;

    @Builder
    private PinRedisRes(
            Long courseId,
            UUID pinId,
            String pinName,
            String originName,
            Double latitude,
            Double longitude,
            String address,
            String sequence) {
        this.courseId = courseId;
        this.pinId = pinId;
        this.pinName = pinName;
        this.originName = originName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.sequence = sequence;
    }

    @Override
    public int compareTo(PinRedisRes other) {
        LexoRank thisRank = LexoRank.parse(this.sequence);
        LexoRank otherRank = LexoRank.parse(other.sequence);
        return thisRank.compareTo(otherRank);
    }

    @Override
    public String toString() {
        return "PinRedisRes{"
                + "courseId="
                + courseId
                + ", pinId="
                + pinId
                + ", pinName='"
                + pinName
                + '\''
                + ", originName='"
                + originName
                + '\''
                + ", latitude="
                + latitude
                + ", longitude="
                + longitude
                + ", address='"
                + address
                + '\''
                + ", sequence='"
                + sequence
                + '\''
                + '}';
    }
}
