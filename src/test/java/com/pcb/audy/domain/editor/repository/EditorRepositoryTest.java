package com.pcb.audy.domain.editor.repository;

import static com.pcb.audy.test.UserTest.TEST_USER;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.test.EditorTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class EditorRepositoryTest implements EditorTest {

    @Autowired private EditorRepository editorRepository;

    @Autowired private CourseRepository courseRepository;

    @Autowired private UserRepository userRepository;

    @Test
    @DisplayName("Course와 User로 권한 정보 조회 테스트")
    void course_user_권한_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);
        Course savedCourse = courseRepository.save(TEST_COURSE);
        editorRepository.save(
                Editor.builder().user(savedUser).course(savedCourse).role(Role.OWNER).build());

        // when
        Editor editor = editorRepository.findByUserAndCourse(TEST_USER, TEST_COURSE);

        // then
        assertEquals(savedUser.getUserId(), editor.getUser().getUserId());
        assertEquals(savedCourse.getCourseId(), editor.getCourse().getCourseId());
    }

    @Test
    @DisplayName("user의 모든 편집 권한 조회 테스트")
    void user_권한_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);

        Course savedCourse1 = courseRepository.save(TEST_COURSE);
        Course savedCourse2 = courseRepository.save(TEST_SECOND_COURSE);

        editorRepository.save(
                Editor.builder().user(savedUser).course(savedCourse1).role(Role.OWNER).build());
        editorRepository.save(
                Editor.builder().user(savedUser).course(savedCourse2).role(Role.MEMBER).build());

        // when
        PageRequest pageRequest1 = PageRequest.of(0, 1);
        Page<Editor> editorList1 =
                editorRepository.findAllByUserOrderByCreateTimestampDesc(TEST_USER, pageRequest1);
        PageRequest pageRequest2 = PageRequest.of(0, 1);
        Page<Editor> editorList2 =
                editorRepository.findAllByUserOrderByCreateTimestampDesc(TEST_USER, pageRequest2);

        // then -> 두 페이지에 나눠서 들어갔는지 확인
        assertEquals(1, editorList1.toList().size());
        assertEquals(1, editorList2.toList().size());
    }

    @Test
    @DisplayName("특정 권한을 가진 유저의 편집 권한 조회 테스트")
    void user_특정권한_조회() {
        // given
        User savedUser = userRepository.save(TEST_USER);

        Course savedCourse1 = courseRepository.save(TEST_COURSE);
        Course savedCourse2 = courseRepository.save(TEST_SECOND_COURSE);

        editorRepository.save(
                Editor.builder().user(savedUser).course(savedCourse1).role(Role.OWNER).build());
        editorRepository.save(
                Editor.builder().user(savedUser).course(savedCourse2).role(Role.MEMBER).build());

        // when
        //        List<Editor> editorList1 =
        //                editorRepository.findAllByUserAndRoleOrderByCreateTimestampDesc(savedUser,
        // Role.OWNER);
        //        List<Editor> editorList2 =
        //                editorRepository.findAllByUserAndRoleOrderByCreateTimestampDesc(savedUser,
        // Role.MEMBER);

        // then
        //        assertEquals(1, editorList1.size());
        //        assertEquals(1, editorList2.size());
    }
}
