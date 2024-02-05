package com.pcb.audy.domain.course.service;

import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_USER;
import static com.pcb.audy.test.UserTest.TEST_USER;
import static com.pcb.audy.test.UserTest.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest implements PinTest {
    @InjectMocks private CourseService courseService;
    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;

    @Captor ArgumentCaptor<Course> argumentCaptor;

    @Nested
    class course_저장 {
        @Test
        @DisplayName("course 저장 테스트")
        void course_저장() {
            // given
            CourseSaveReq courseSaveReq =
                    CourseSaveReq.builder().userId(TEST_USER_ID).courseName(TEST_COURSE_NAME).build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);

            // when
            courseService.saveCourse(courseSaveReq);

            // then
            verify(userRepository).findByUserId(any());
            verify(courseRepository).save(argumentCaptor.capture());
            assertEquals(TEST_USER, argumentCaptor.getValue().getEditorList().get(0).getUser());
            assertEquals(Role.OWNER, argumentCaptor.getValue().getEditorList().get(0).getRole());
        }

        @Test
        @DisplayName("course 저장 실패 테스트 - 사용자 권한 없음")
        void invalidUserSaveCourseTest() {
            // given
            CourseSaveReq courseSaveReq =
                    CourseSaveReq.builder().userId(TEST_USER_ID).courseName(TEST_COURSE_NAME).build();

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                courseService.saveCourse(courseSaveReq);
                            });

            // then
            assertEquals(NOT_FOUND_USER, exception.getResultCode());
        }
    }
}
