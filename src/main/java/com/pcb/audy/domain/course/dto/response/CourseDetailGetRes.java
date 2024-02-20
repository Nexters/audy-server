package com.pcb.audy.domain.course.dto.response;

import com.pcb.audy.domain.pin.dto.response.PinGetRes;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetailGetRes {

    private Long courseId;
    private String courseName;
    private List<PinGetRes> pinResList;

    @Builder
    private CourseDetailGetRes(Long courseId, String courseName, List<PinGetRes> pinResList) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.pinResList = pinResList;
    }
}
