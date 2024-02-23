package com.pcb.audy.domain.course.service;

import com.pcb.audy.domain.course.dto.response.CourseDetailGetRes;
import com.pcb.audy.domain.course.dto.response.CourseGetRes;
import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.pin.dto.response.PinGetRes;
import com.pcb.audy.domain.pin.dto.response.PinRedisRes;
import com.pcb.audy.domain.pin.dto.response.PinSaveRes;
import com.pcb.audy.domain.pin.entity.Pin;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseServiceMapper {
    CourseServiceMapper INSTANCE = Mappers.getMapper(CourseServiceMapper.class);

    CourseSaveRes toCourseSaveRes(Course course);

    @Mapping(target = "courseId", expression = "java(editor.getCourse().getCourseId())")
    @Mapping(target = "courseName", expression = "java(editor.getCourse().getCourseName())")
    @Mapping(
            target = "pinCnt",
            expression =
                    "java(editor.getCourse().getPinList() != null ? editor.getCourse().getPinList().size() : 0)")
    @Mapping(
            target = "editorCnt",
            expression =
                    "java(editor.getCourse().getEditorList() != null ? editor.getCourse().getEditorList().size() : 0)")
    @Mapping(
            target = "isOwner",
            expression = "java(com.pcb.audy.global.meta.Role.OWNER.equals(editor.getRole()))")
    CourseGetRes toCourseGetRes(Editor editor);

    List<CourseGetRes> toCourseGetResList(List<Editor> editor);

    PinGetRes redisToPinGetRes(PinSaveRes pin);

    List<PinSaveRes> toPinGetRes(List<Pin> pin);

    @Mapping(target = "courseId", expression = "java(pin.getCourse().getCourseId())")
    PinRedisRes toPinRedisRes(Pin pin);

    List<PinRedisRes> toPinRedisResList(List<Pin> pinList);

    CourseDetailGetRes toCourseDetailGetRes(Course course, List<PinRedisRes> pinResList);
}
