package com.pcb.audy.domain.course.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.test.CourseTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CourseRepositoryTest implements CourseTest {
    @Autowired private CourseRepository courseRepository;

    @Test
    @DisplayName("courseId로 코스 조회 테스트")
    void courseId_코스_조회() {
        // given
        Course savedCourse = courseRepository.save(TEST_COURSE);

        // when
        Course course = courseRepository.findByCourseId(savedCourse.getCourseId());

        // then
        assertThat(course.getCourseId()).isEqualTo(savedCourse.getCourseId());
        assertThat(course.getCourseName()).isEqualTo(savedCourse.getCourseName());
    }
}
