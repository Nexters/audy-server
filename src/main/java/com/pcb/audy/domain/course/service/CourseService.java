package com.pcb.audy.domain.course.service;

import com.pcb.audy.domain.course.dto.request.CourseDeleteReq;
import com.pcb.audy.domain.course.dto.request.CourseInviteReq;
import com.pcb.audy.domain.course.dto.request.CourseSaveReq;
import com.pcb.audy.domain.course.dto.request.CourseUpdateReq;
import com.pcb.audy.domain.course.dto.response.*;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.EditorValidator;
import com.pcb.audy.global.validator.UserValidator;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EditorRepository editorRepository;
    private final RedisProvider redisProvider;

    public static final String INVITE_PREFIX = "invite:";
    public static final String COURSE_PREFIX = "course:";
    public static final String DOMAIN = "https://audy-gakka.com/invite/";
    public static final int INVITE_EXPIRE_TIME = 72 * 60 * 60 * 1000;

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

    @Transactional
    public CourseDeleteRes deleteCourse(CourseDeleteReq courseDeleteReq) {
        User user = getUserByUserId(courseDeleteReq.getUserId());
        Course course = getCourseByCourseId(courseDeleteReq.getCourseId());

        isAdminUser(user, course);
        courseRepository.delete(course);

        return new CourseDeleteRes();
    }

    @Transactional(readOnly = true)
    public CourseInviteRes inviteCourse(CourseInviteReq courseInviteReq) {
        // 초대 권한 확인
        User user = getUserByUserId(courseInviteReq.getUserId());
        Course course = getCourseByCourseId(courseInviteReq.getCourseId());
        isAdminUser(user, course);

        // 링크 생성
        String redisKey = INVITE_PREFIX + courseInviteReq.getCourseId();
        String key = (String) redisProvider.get(redisKey);
        if (key == null) {
            String before =
                    COURSE_PREFIX + courseInviteReq.getCourseId() + RandomStringUtils.randomAlphabetic(6);
            key = Base64.getEncoder().encodeToString(before.getBytes());
            redisProvider.set(redisKey, key, INVITE_EXPIRE_TIME);
        }

        return CourseInviteRes.builder().url(DOMAIN + key).build();
    }

    @Transactional(readOnly = true)
    public CourseDetailGetRes getCourse(Long courseId) {
        Course course = getCourseByCourseId(courseId);
        return CourseServiceMapper.INSTANCE.toCourseDetailGetRes(course);
    }

    @Transactional(readOnly = true)
    public CourseGetResList getAllCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Editor> editors =
                editorRepository.findAllByUserOrderByCreateTimestampDesc(user, pageable).toList();
        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors))
                .build();
    }

    @Transactional(readOnly = true)
    public CourseGetResList getOwnedCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Editor> editors =
                editorRepository
                        .findAllByUserAndRoleOrderByCreateTimestampDesc(user, Role.OWNER, pageable)
                        .toList();
        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors))
                .build();
    }

    @Transactional(readOnly = true)
    public CourseGetResList getMemberCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        List<Editor> editors =
                editorRepository
                        .findAllByUserAndRoleOrderByCreateTimestampDesc(user, Role.MEMBER, pageable)
                        .toList();
        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors))
                .build();
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
        EditorValidator.checkIsAdminUser(editor);
    }
}
