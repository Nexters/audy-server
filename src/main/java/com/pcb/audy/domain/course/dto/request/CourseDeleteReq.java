package com.pcb.audy.domain.course.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDeleteReq {

    private Long userId;
    private Long courseId;

    @Builder
    private CourseDeleteReq(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }
}
