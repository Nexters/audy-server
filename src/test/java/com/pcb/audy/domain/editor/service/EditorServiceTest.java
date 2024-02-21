package com.pcb.audy.domain.editor.service;

import static com.pcb.audy.global.meta.Role.MEMBER;
import static com.pcb.audy.global.response.ResultCode.ALREADY_EXIST_EDITOR;
import static com.pcb.audy.global.response.ResultCode.NOT_ADMIN_COURSE;
import static com.pcb.audy.global.response.ResultCode.VALID_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.request.EditorSaveReq;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.test.EditorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EditorServiceTest implements EditorTest {
    @InjectMocks private EditorService editorService;

    @Mock private RedisProvider redisProvider;
    @Mock private EditorRepository editorRepository;
    @Mock private UserRepository userRepository;
    @Mock private CourseRepository courseRepository;

    @Nested
    class editor_저장 {
        @Test
        @DisplayName("editor 저장 테스트")
        void editor_저장() {
            // given
            EditorSaveReq editorSaveReq =
                    EditorSaveReq.builder().courseId(TEST_COURSE_ID).key(TEST_KEY).build();
            when(redisProvider.get(any())).thenReturn(TEST_KEY);
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(null);

            // when
            editorService.saveEditor(editorSaveReq);

            // then
            verify(redisProvider).get(any());
            verify(userRepository).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            verify(editorRepository).save(any());
        }

        @Test
        @DisplayName("editor 저장 실패 테스트 (key match)")
        void editor_저장_실패_key_match() {
            // given
            EditorSaveReq editorSaveReq =
                    EditorSaveReq.builder().courseId(TEST_COURSE_ID).key(TEST_ANOTHER_KEY).build();
            when(redisProvider.get(any())).thenReturn(TEST_KEY);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                editorService.saveEditor(editorSaveReq);
                            });

            // then
            verify(redisProvider).get(any());
            assertThat(exception.getResultCode()).isEqualTo(VALID_KEY);
        }

        @Test
        @DisplayName("editor 저장 실패 테스트 (already exist)")
        void editor_저장_실패_already_exist() {
            // given
            EditorSaveReq editorSaveReq =
                    EditorSaveReq.builder().courseId(TEST_COURSE_ID).key(TEST_KEY).build();
            when(redisProvider.get(any())).thenReturn(TEST_KEY);
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_MEMBER);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                editorService.saveEditor(editorSaveReq);
                            });

            // then
            verify(redisProvider).get(any());
            verify(userRepository).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            assertThat(exception.getResultCode()).isEqualTo(ALREADY_EXIST_EDITOR);
        }
    }

    @Nested
    class editor_역할_수정 {
        @Test
        @DisplayName("editor 역할 수정 테스트")
        void editor_역할_수정() {
            // given
            EditorRoleUpdateReq editorRoleUpdateReq =
                    EditorRoleUpdateReq.builder()
                            .courseId(TEST_COURSE_ID)
                            .selectedUserId(TEST_ANOTHER_USER_ID)
                            .role(MEMBER)
                            .build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER).thenReturn(TEST_ANOTHER_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_ADMIN);

            // when
            editorService.updateRoleEditor(editorRoleUpdateReq);

            // then
            verify(userRepository, times(2)).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            verify(editorRepository).save(any());
        }

        @Test
        @DisplayName("editor 역할 수정 실패 테스트")
        void editor_역할_수정_실패() {
            // given
            EditorRoleUpdateReq editorRoleUpdateReq =
                    EditorRoleUpdateReq.builder()
                            .courseId(TEST_COURSE_ID)
                            .selectedUserId(TEST_ANOTHER_USER_ID)
                            .role(MEMBER)
                            .build();
            when(userRepository.findByUserId(any())).thenReturn(TEST_USER);
            when(courseRepository.findByCourseId(any())).thenReturn(TEST_COURSE);
            when(editorRepository.findByUserAndCourse(any(), any())).thenReturn(TEST_EDITOR_MEMBER);

            // when
            GlobalException exception =
                    assertThrows(
                            GlobalException.class,
                            () -> {
                                editorService.updateRoleEditor(editorRoleUpdateReq);
                            });

            // then
            verify(userRepository).findByUserId(any());
            verify(courseRepository).findByCourseId(any());
            verify(editorRepository).findByUserAndCourse(any(), any());
            assertThat(exception.getResultCode()).isEqualTo(NOT_ADMIN_COURSE);
        }
    }
}
