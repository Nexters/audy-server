package com.pcb.audy.domain.editor.service;

import com.pcb.audy.domain.editor.dto.response.EditorSaveRes;
import com.pcb.audy.domain.editor.entity.Editor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EditorServiceMapper {
    EditorServiceMapper INSTANCE = Mappers.getMapper(EditorServiceMapper.class);

    @Mapping(target = "courseId", expression = "java(editor.getCourse().getCourseId())")
    EditorSaveRes toEditorSaveRes(Editor editor);
}
