package com.pcb.audy.domain.editor.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorSaveReq {
    private Long userId;
    private Long courseId;
    private String key;

    @Builder
    private EditorSaveReq(Long courseId, String key) {
        this.courseId = courseId;
        this.key = key;
    }
}
