package com.pcb.audy.domain.editor.service;

import static com.pcb.audy.global.meta.Role.MEMBER;

import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.dto.request.EditorSaveReq;
import com.pcb.audy.domain.editor.dto.response.EditorSaveRes;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.EditorValidator;
import com.pcb.audy.global.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EditorService {
    private final RedisProvider redisProvider;
    private final EditorRepository editorRepository;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    private static final String INVITE_PREFIX = "invite:";

    @Transactional
    public EditorSaveRes saveEditor(EditorSaveReq editorSaveReq) {
        EditorValidator.validateKey(
                editorSaveReq.getKey(),
                (String) redisProvider.get(INVITE_PREFIX + editorSaveReq.getCourseId()));
        User user = getUserByUserId(editorSaveReq.getUserId());
        Course course = getCourseByCourseId(editorSaveReq.getCourseId());
        checkAlreadyExistEditor(user, course);
        editorRepository.save(Editor.builder().user(user).course(course).role(MEMBER).build());
        return new EditorSaveRes();
    }

    private void checkAlreadyExistEditor(User user, Course course) {
        Editor editor = editorRepository.findByUserAndCourse(user, course);
        EditorValidator.checkAlreadyExist(editor);
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
}
