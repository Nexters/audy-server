package com.pcb.audy.domain.course.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInviteRedisReq {
    private Long courseId;

    @Builder
    private CourseInviteRedisReq(Long courseId) {
        this.courseId = courseId;
    }
}
