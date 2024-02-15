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
    private boolean isLast;

    @Builder
    private CourseGetResList(List<CourseGetRes> courseGetResList, boolean isLast) {
        this.courseGetResList = courseGetResList;
        this.isLast = isLast;
    }
}
