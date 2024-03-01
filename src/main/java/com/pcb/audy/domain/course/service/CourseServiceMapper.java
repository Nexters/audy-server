package com.pcb.audy.domain.course.service;

import com.pcb.audy.domain.course.dto.response.CourseGetRes;
import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.editor.dto.response.EditorGetRes;
import com.pcb.audy.domain.editor.entity.Editor;
import java.util.List;
import org.mapstruct.*;
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

    @Mapping(target = "userId", expression = "java(editor.getUser().getUserId())")
    @Mapping(target = "userName", expression = "java(editor.getUser().getUsername())")
    @Mapping(target = "imageUrl", expression = "java(editor.getUser().getImageUrl())")
    EditorGetRes toEditorGetRes(Editor editor);

    List<EditorGetRes> toEditorGetResList(List<Editor> editor);
}
