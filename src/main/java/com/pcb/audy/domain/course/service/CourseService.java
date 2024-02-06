package com.pcb.audy.domain.course.service;

import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.CourseSaveRes;
import com.pcb.audy.domain.course.dto.response.CourseUpdateRes;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.EditorValidator;
import com.pcb.audy.global.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EditorRepository editorRepository;

    @Transactional
    public CourseSaveRes saveCourse(CourseSaveReq commentSaveReq) {
        User user = getUserByUserId(commentSaveReq.getUserId());
        Course savedCourse =
                courseRepository.save(Course.builder().courseName(commentSaveReq.getCourseName()).build());

        Editor editor = Editor.builder().course(savedCourse).user(user).role(Role.OWNER).build();
        editorRepository.save(editor);
        return CourseServiceMapper.INSTANCE.toCourseSaveRes(savedCourse);
    }

    @Transactional
    public CourseUpdateRes updateCourseName(CourseUpdateReq courseUpdateReq) {
        User user = getUserByUserId(courseUpdateReq.getUserId());
        Course course = getCourseByCourseId(courseUpdateReq.getCourseId());

        isAdminUser(user, course);
        courseRepository.save(
                Course.builder()
                        .courseId(courseUpdateReq.getCourseId())
                        .courseName(courseUpdateReq.getCourseName())
                        .build());

        return new CourseUpdateRes();
    }

    private User getUserByUserId(Long userId) {
        User user = userRepository.findByUserId(userId);
        UserValidator.validate(user);
        return user;
    }

    private Course getCourseByCourseId(Long courseId) {
        Course course = courseRepository.findByCourseId(courseId);
        CourseValidator.validate(course);
        return course;
    }

    private void isAdminUser(User user, Course course) {
        Editor editor = editorRepository.findByUserAndCourse(user, course);
        EditorValidator.validate(editor);
        EditorValidator.checkIsAdminUser(editor);
    }
}
