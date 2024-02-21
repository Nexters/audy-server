package com.pcb.audy.domain.course.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInviteRedisReq {
    private Long courseId;
    private Long userId;

    @Builder
    private CourseInviteRedisReq(Long userId, Long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        CourseInviteRedisReq courseInviteRedisReq = (CourseInviteRedisReq) o;
        return courseInviteRedisReq.getUserId().equals(this.userId)
                && courseInviteRedisReq.getCourseId().equals(this.courseId);
    }
}
