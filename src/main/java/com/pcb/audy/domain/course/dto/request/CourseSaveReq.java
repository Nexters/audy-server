package com.pcb.audy.domain.course.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseSaveReq {

    private Long userId;
    private String courseName;

    @Builder
    private CourseSaveReq(Long userId, String courseName) {
        this.userId = userId;
        this.courseName = courseName;
    }
}
