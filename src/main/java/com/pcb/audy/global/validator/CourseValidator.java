package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_COURSE;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.global.exception.GlobalException;

public class CourseValidator {
    public static void validate(Course course) {
        if (!isExistCourse(course)) {
            throw new GlobalException(NOT_FOUND_COURSE);
        }
    }

    private static boolean isExistCourse(Course course) {
        return course != null;
    }
}
