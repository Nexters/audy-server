package com.pcb.audy.domain.course.service;

import static com.pcb.audy.global.response.ResultCode.NOT_ADMIN_COURSE;
import static com.pcb.audy.global.response.ResultCode.NOT_FOUND_USER;
import static com.pcb.audy.test.EditorTest.TEST_EDITOR_ADMIN;
import static com.pcb.audy.test.EditorTest.TEST_EDITOR_MEMBER;
import static com.pcb.audy.test.UserTest.TEST_USER;
import static com.pcb.audy.test.UserTest.TEST_USER_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.pcb.audy.domain.course.dto.request.CourseDeleteReq;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.CourseDetailGetRes;
import com.pcb.audy.domain.course.dto.response.CourseGetRes;
import com.pcb.audy.domain.course.dto.response.CourseGetResList;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.test.PinTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest implements PinTest {
    @InjectMocks private CourseService courseService;
    @Mock private CourseRepository courseRepository;
    @Mock private UserRepository userRepository;
    @Mock private EditorRepository editorRepository;

    @Nested
    class course_저장 {
        @Test
        @DisplayName("course 저장 테스트")
        void course_저장() {
            // given
            CourseSaveReq courseSaveReq =
                    CourseSaveReq.builder().userId(TEST_USER_ID).courseName(TEST_COURSE_NAME).build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.save(any())).thenReturn(TEST_COURSE);

            // when
            courseService.saveCourse(courseSaveReq);

            // then
            verify(userRepository).findByUserId(any());
            verify(courseRepository, times(1)).save(any(Course.class));

            ArgumentCaptor<Editor> editorCaptor = ArgumentCaptor.forClass(Editor.class);
            verify(editorRepository).save(editorCaptor.capture());

            Editor capturedEditor = editorCaptor.getValue();
            assertEquals(TEST_USER, capturedEditor.getUser());
            assertEquals(Role.OWNER, capturedEditor.getRole());

            assertNotNull(capturedEditor.getCourse());
            assertEquals(TEST_COURSE, capturedEditor.getCourse());
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

    @Nested
    class course_이름수정 {
        @Test
        @DisplayName("course 수정 테스트")
        void course_수정() {
            // given
            CourseUpdateReq courseUpdateReq =
                    CourseUpdateReq.builder()
                            .courseId(TEST_COURSE_ID)
                            .courseName(TEST_UPDATED_COURSE_NAME)
                            .build();

            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_ADMIN);

            // when
            courseService.updateCourseName(courseUpdateReq);

            // then
            verify(userRepository).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            verify(courseRepository).save(any());
        }

        @Test
        @DisplayName("course 수정 실패 테스트 - 사용자 권한 없음")
        void course_수정_실패() {
            // given
            CourseUpdateReq courseUpdateReq =
                    CourseUpdateReq.builder()
                            .userId(TEST_USER_ID)
                            .courseId(TEST_COURSE_ID)
                            .courseName(TEST_UPDATED_COURSE_NAME)
                            .build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_MEMBER);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                courseService.updateCourseName(courseUpdateReq);
                            });

            // then
            assertEquals(NOT_ADMIN_COURSE, exception.getResultCode());
        }
    }

    @Nested
    class course_삭제 {
        @Test
        @DisplayName("course 삭제 테스트")
        void course_삭제() {
            // given
            CourseDeleteReq courseDeleteReq = CourseDeleteReq.builder().courseId(TEST_COURSE_ID).build();

            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_ADMIN);

            // when
            courseService.deleteCourse(courseDeleteReq);

            // then
            verify(userRepository).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            verify(courseRepository).delete(any());
        }

        @Test
        @DisplayName("course 삭제 테스트 - 사용자 권한 없음")
        void course_삭제_실패() {
            // given
            CourseDeleteReq courseDeleteReq = CourseDeleteReq.builder().courseId(TEST_COURSE_ID).build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_MEMBER);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                courseService.deleteCourse(courseDeleteReq);
                            });

            // then
            assertEquals(NOT_ADMIN_COURSE, exception.getResultCode());
        }
    }

    @Test
    @DisplayName("course 전체 조회 테스트")
    void course_전체_조회() {
        // given
        List<Editor> editorList = new ArrayList<>();
        editorList.add(TEST_EDITOR_MEMBER);
        editorList.add(TEST_EDITOR_ADMIN);

        when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
        when(editorRepository.findAllByUser(any())).thenReturn(editorList);

        // when
        CourseGetResList courseGetResList = courseService.getAllCourse(TEST_USER_ID);

        // then
        verify(userRepository).findByUserId(any());
        verify(editorRepository).findAllByUser(any());
        assertEquals(2, courseGetResList.getCourseGetResList().size());
    }

    @Test
    @DisplayName("admin course 조회 테스트")
    void admin_course_전체_조회() {
        // given
        List<Editor> editorList = new ArrayList<>();
        editorList.add(TEST_EDITOR_ADMIN);

        when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
        when(editorRepository.findAllByUserAndRole(any(), eq(Role.OWNER))).thenReturn(editorList);

        // when
        CourseGetResList courseGetResList = courseService.getOwnedCourse(TEST_USER_ID);

        // then
        verify(userRepository).findByUserId(any());
        verify(editorRepository).findAllByUserAndRole(any(), eq(Role.OWNER));
        assertEquals(1, courseGetResList.getCourseGetResList().size());
    }

    @Test
    @DisplayName("member course 조회 테스트")
    void member_course_전체_조회() {
        // given
        List<Editor> editorList = new ArrayList<>();
        editorList.add(TEST_EDITOR_MEMBER);

        when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
        when(editorRepository.findAllByUserAndRole(any(), eq(Role.MEMBER))).thenReturn(editorList);

        // when
        CourseGetResList courseGetResList = courseService.getMemberCourse(TEST_USER_ID);

        // then
        verify(userRepository).findByUserId(any());
        verify(editorRepository).findAllByUserAndRole(any(), eq(Role.MEMBER));
        assertEquals(1, courseGetResList.getCourseGetResList().size());
    }

    @Test
    @DisplayName("course 상세 테스트")
    void course_상세_조회() {
        // given
        when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);

        // when
        CourseDetailGetRes courseDetailGetRes = courseService.getCourse(TEST_COURSE_ID);

        // then
        verify(courseRepository).findByCourseId(any());
        assertEquals(TEST_COURSE_ID, courseDetailGetRes.getCourseId());
    }
}
