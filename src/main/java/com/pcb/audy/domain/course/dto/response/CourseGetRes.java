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

    @Builder
    private CourseGetRes(Long courseId, String courseName, int pinCnt, int editorCnt) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.pinCnt = pinCnt;
        this.editorCnt = editorCnt;
    }
}
