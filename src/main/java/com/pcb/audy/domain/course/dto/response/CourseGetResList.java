package com.pcb.audy.domain.course.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseGetResList {
    private List<CourseGetRes> courseGetResList;

    @Builder
    private CourseGetResList(List<CourseGetRes> courseGetResList) {
        this.courseGetResList = courseGetResList;
    }
}
