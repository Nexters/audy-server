package com.pcb.audy.domain.editor.service;

import static com.pcb.audy.global.meta.Role.MEMBER;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.course.dto.request.CourseInviteRedisReq;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.dto.request.EditorDeleteReq;
import com.pcb.audy.domain.editor.dto.request.EditorRoleUpdateReq;
import com.pcb.audy.domain.editor.dto.request.EditorSaveReq;
import com.pcb.audy.domain.editor.dto.response.EditorDeleteRes;
import com.pcb.audy.domain.editor.dto.response.EditorRoleUpdateRes;
import com.pcb.audy.domain.editor.dto.response.EditorSaveRes;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.util.InviteUtil;
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
    private final InviteUtil inviteUtil;
    private final ObjectMapper objectMapper;

    private static final String INVITE_PREFIX = "Invite: ";

    @Transactional
    public EditorSaveRes saveEditor(EditorSaveReq editorSaveReq) {

        String key = editorSaveReq.getKey();
        CourseInviteRedisReq courseInviteRedisReq = inviteUtil.decryptCourseInviteReq(key);

        // 초대 객체가 Redis에 있는 지 확인
        CourseInviteRedisReq findByKey =
                objectMapper.convertValue(
                        redisProvider.get(INVITE_PREFIX + courseInviteRedisReq.getCourseId()),
                        CourseInviteRedisReq.class);
        EditorValidator.checkValidateObject(courseInviteRedisReq, findByKey);

        // 초대 링크 상의 course가 유효한 지 검증
        Course course = getCourseByCourseId(courseInviteRedisReq.getCourseId());

        // 유저에 Editor 추가 가능한 지 검증
        User user = getUserByUserId(editorSaveReq.getUserId());
        checkAlreadyExistEditor(user, course);

        Editor savedEditor =
                editorRepository.save(Editor.builder().user(user).course(course).role(MEMBER).build());
        return EditorServiceMapper.INSTANCE.toEditorSaveRes(savedEditor);
    }

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

    public EditorDeleteRes deleteEditor(EditorDeleteReq editorDeleteReq) {
        User admin = getUserByUserId(editorDeleteReq.getUserId());
        User member = getUserByUserId(editorDeleteReq.getSelectedUserId());
        Course course = getCourseByCourseId(editorDeleteReq.getCourseId());

        Editor adminEditor = getEditor(admin, course);
        Editor targetEditor = getEditor(member, course);
        EditorValidator.checkIsAdminUser(adminEditor);
        EditorValidator.checkIsMemberUser(targetEditor);

        editorRepository.delete(targetEditor);
        return new EditorDeleteRes();
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

    private void checkAlreadyExistEditor(User user, Course course) {
        Editor editor = editorRepository.findByUserAndCourse(user, course);
        EditorValidator.checkAlreadyExist(editor);
    }
}
