package com.pcb.audy.domain.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSaveRes {

    private Long courseId;

    @Builder
    private CourseSaveRes(Long courseId) {
        this.courseId = courseId;
    }
}
