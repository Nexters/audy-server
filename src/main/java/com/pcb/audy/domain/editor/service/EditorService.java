package com.pcb.audy.domain.editor.service;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.response.EditorRoleUpdateRes;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.EditorValidator;
import com.pcb.audy.global.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final EditorRepository editorRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public EditorRoleUpdateRes updateRoleEditor(EditorRoleUpdateReq editorRoleUpdateReq) {
        User user = getUserByUserId(editorRoleUpdateReq.getUserId());
        Course course = getCourseByCourseId(editorRoleUpdateReq.getCourseId());
        Editor editor = getEditor(user, course);
        EditorValidator.checkIsAdminUser(editor);

        User selectedUser = getUserByUserId(editorRoleUpdateReq.getSelectedUserId());
        editorRepository.save(
                Editor.builder()
                        .user(selectedUser)
                        .course(course)
                        .role(editorRoleUpdateReq.getRole())
                        .build());
        return new EditorRoleUpdateRes();
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

    private Editor getEditor(User user, Course course) {
        Editor editor = editorRepository.findByUserAndCourse(user, course);
        EditorValidator.validate(editor);
        return editor;
    }
}
