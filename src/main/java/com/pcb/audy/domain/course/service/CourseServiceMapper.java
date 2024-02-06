package com.pcb.audy.domain.course.service;

import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseServiceMapper {
    CourseServiceMapper INSTANCE = Mappers.getMapper(CourseServiceMapper.class);

    CourseSaveRes toCourseSaveRes(Course course);
}
