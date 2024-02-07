package com.pcb.audy.domain.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseGetRes {

    private Long courseId;
    private String courseName;
    private int pinCnt;
    private int editorCnt;
    private boolean isOwner;

    @Builder
    private CourseGetRes(
            Long courseId, String courseName, int pinCnt, int editorCnt, boolean isOwner) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.pinCnt = pinCnt;
        this.editorCnt = editorCnt;
        this.isOwner = isOwner;
    }
}
