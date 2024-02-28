package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_COURSE;
import static com.pcb.audy.global.response.ResultCode.VALID_COURSE_NAME;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.global.exception.GlobalException;
import org.springframework.util.StringUtils;

public class CourseValidator {
    private static final int MAX_LENGTH = 10;

    public static void validate(Course course) {
        if (!isExistCourse(course)) {
            throw new GlobalException(NOT_FOUND_COURSE);
        }
    }

    public static void validateName(String name) {
        if (!isValidName(name)) {
            throw new GlobalException(VALID_COURSE_NAME);
        }
    }

    private static boolean isExistCourse(Course course) {
        return course != null;
    }

    private static boolean isValidName(String name) {
        return StringUtils.hasLength(name) && name.length() <= MAX_LENGTH;
    }
}
