package com.pcb.audy.test;

import com.pcb.audy.domain.course.entity.Course;

public interface CourseTest {

    Long TEST_COURSE_ID = 1L;
    String TEST_COURSE_NAME = "course";
    String TEST_UPDATED_COURSE_NAME = "updatedName";

    Long TEST_SECOND_COURSE_ID = 2L;
    String TEST_SECOND_COURSE_NAME = "course2";
    Course TEST_COURSE =
            Course.builder().courseId(TEST_COURSE_ID).courseName(TEST_COURSE_NAME).build();

    Course TEST_SECOND_COURSE =
            Course.builder().courseId(TEST_SECOND_COURSE_ID).courseName(TEST_SECOND_COURSE_NAME).build();
}
