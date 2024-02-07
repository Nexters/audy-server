package com.pcb.audy.domain.course.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInviteReq {

    private Long userId;
    private Long courseId;

    @Builder
    private CourseInviteReq(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }
}
