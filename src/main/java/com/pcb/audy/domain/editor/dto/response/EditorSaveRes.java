package com.pcb.audy.domain.editor.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorSaveRes {
    private Long courseId;

    @Builder
    private EditorSaveRes(Long courseId) {
        this.courseId = courseId;
    }
}
