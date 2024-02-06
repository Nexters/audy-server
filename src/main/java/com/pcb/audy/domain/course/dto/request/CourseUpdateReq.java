package com.pcb.audy.domain.course.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseUpdateReq {

    private Long userId;
    private Long courseId;
    private String courseName;

    @Builder
    private CourseUpdateReq(Long userId, Long courseId, String courseName) {
        this.userId = userId;
        this.courseId = courseId;
        this.courseName = courseName;
    }
}
