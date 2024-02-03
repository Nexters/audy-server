package com.pcb.audy.domain.course.repository;

import com.pcb.audy.domain.course.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Long> {
    Course findByCourseId(Long courseId);
}
