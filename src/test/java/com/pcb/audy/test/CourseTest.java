package com.pcb.audy.test;

import static com.pcb.audy.test.UserTest.TEST_USER_ID;

import com.pcb.audy.domain.course.dto.request.CourseInviteRedisReq;
import com.pcb.audy.domain.course.entity.Course;

public interface CourseTest {

    Long TEST_COURSE_ID = 1L;
    String TEST_COURSE_NAME = "course";
    String TEST_UPDATED_COURSE_NAME = "updateName";

    Long TEST_SECOND_COURSE_ID = 2L;
    String TEST_SECOND_COURSE_NAME = "course2";

    String TEST_INVITE_URL = "https://audy-gakka.com/invite/Y291cnNlM3BqdGtEVA==";

    Course TEST_COURSE =
            Course.builder().courseId(TEST_COURSE_ID).courseName(TEST_COURSE_NAME).build();

    Course TEST_SECOND_COURSE =
            Course.builder().courseId(TEST_SECOND_COURSE_ID).courseName(TEST_SECOND_COURSE_NAME).build();

    CourseInviteRedisReq TEST_COURSE_INVITE_REDIS_REQ =
            CourseInviteRedisReq.builder().courseId(TEST_COURSE_ID).userId(TEST_USER_ID).build();
}
