package com.pcb.audy.domain.sample.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleGetResList {

    private List<SampleGetRes> sampleGetReses;
    private int total;

    @Builder
    private SampleGetResList(List<SampleGetRes> sampleGetReses, int total) {
        this.sampleGetReses = sampleGetReses;
        this.total = total;
    }
}
