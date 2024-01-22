package com.pcb.audy.domain.sample.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SampleGetRes {

    private Long sampleId;
    private String title;
    private String text;

    @Builder
    private SampleGetRes(Long sampleId, String title, String text) {
        this.sampleId = sampleId;
        this.title = title;
        this.text = text;
    }
}
