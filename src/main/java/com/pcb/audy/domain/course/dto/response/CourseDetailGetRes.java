package com.pcb.audy.domain.course.dto.response;

import com.pcb.audy.domain.editor.dto.response.EditorGetRes;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseDetailGetRes {

    private Long courseId;
    private String courseName;
    private int pinCnt;
    private int editorCnt;
    private List<EditorGetRes> editorGetResList;
    private List<PinRedisRes> pinResList;

    @Builder
    private CourseDetailGetRes(
            Long courseId,
            String courseName,
            int pinCnt,
            int editorCnt,
            List<EditorGetRes> editorGetResList,
            List<PinRedisRes> pinResList) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.pinCnt = pinCnt;
        this.editorCnt = editorCnt;
        this.editorGetResList = editorGetResList;
        this.pinResList = pinResList;
    }
}
