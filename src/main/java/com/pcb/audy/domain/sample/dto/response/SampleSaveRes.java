package com.pcb.audy.domain.sample.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleSaveRes {
    private Long sampleId;

    @Builder
    private SampleSaveRes(Long sampleId) {
        this.sampleId = sampleId;
    }
}
