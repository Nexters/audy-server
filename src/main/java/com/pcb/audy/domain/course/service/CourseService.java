package com.pcb.audy.domain.course.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pcb.audy.domain.course.dto.request.*;
import com.pcb.audy.domain.course.dto.response.*;
import com.pcb.audy.domain.course.entity.Course;
import com.pcb.audy.domain.course.repository.CourseRepository;
import com.pcb.audy.domain.editor.dto.response.EditorGetRes;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.domain.editor.repository.EditorRepository;
import com.pcb.audy.domain.pin.dto.response.PinGetRes;
import com.pcb.audy.domain.pin.service.PinServiceMapper;
import com.pcb.audy.domain.user.entity.User;
import com.pcb.audy.domain.user.repository.UserRepository;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.global.redis.RedisProvider;
import com.pcb.audy.global.util.InviteUtil;
import com.pcb.audy.global.util.LexoRankUtil;
import com.pcb.audy.global.validator.CourseValidator;
import com.pcb.audy.global.validator.EditorValidator;
import com.pcb.audy.global.validator.UserValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final EditorRepository editorRepository;
    private final RedisProvider redisProvider;
    private final InviteUtil inviteUtil;
    private final LexoRankUtil lexoUtil;
    private final ObjectMapper objectMapper;

    private static final String INVITE_PREFIX = "Invite: ";
    public static final String INVITE_DOMAIN = "https://audy-gakka.com/invite/";
    public static final int INVITE_EXPIRE_TIME = 72 * 60 * 60 * 1000;

    @Transactional
    public CourseSaveRes saveCourse(CourseSaveReq commentSaveReq) {
        CourseValidator.validateName(commentSaveReq.getCourseName());
        User user = getUserByUserId(commentSaveReq.getUserId());
        Course savedCourse =
                courseRepository.save(Course.builder().courseName(commentSaveReq.getCourseName()).build());

        Editor editor = Editor.builder().course(savedCourse).user(user).role(Role.OWNER).build();
        editorRepository.save(editor);
        return CourseServiceMapper.INSTANCE.toCourseSaveRes(savedCourse);
    }

    @Transactional
    public CourseUpdateRes updateCourseName(CourseUpdateReq courseUpdateReq) {
        CourseValidator.validateName(courseUpdateReq.getCourseName());
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
        isExceedEditorLimit(course);

        // 링크 생성
        String redisKey = INVITE_PREFIX + courseInviteReq.getCourseId();
        CourseInviteRedisReq courseInviteRedisReq =
                CourseInviteRedisReq.builder()
                        .userId(courseInviteReq.getUserId())
                        .courseId(courseInviteReq.getCourseId())
                        .build();

        if (!redisProvider.hasKey(redisKey)) { // 중복 체크 -> 하지 않으면 기존 코드가 사용 불가능하기 때문
            redisProvider.set(redisKey, courseInviteRedisReq, INVITE_EXPIRE_TIME);
        }

        return CourseInviteRes.builder()
                .url(INVITE_DOMAIN + inviteUtil.encryptCourseInviteReq(courseInviteRedisReq))
                .build();
    }

    @Transactional(readOnly = true)
    public CourseDetailGetRes getCourse(Long courseId) {
        Course course = getCourseByCourseId(courseId);

        // Redis에서 Pin 조회
        List<PinGetRes> pinResList = lexoUtil.sortByLexoRank(courseId);
        if (CollectionUtils.isEmpty(pinResList)) {
            // Redis에 데이터가 없는 경우, DB에서 가져오기 + Redis에 캐싱
            pinResList = PinServiceMapper.INSTANCE.toPinGetResListFromPin(course.getPinList());
            redisProvider.multiSet(
                    PinServiceMapper.INSTANCE.toPinRedisResListFromPin(course.getPinList()));
        }

        List<EditorGetRes> editorGetResList =
                CourseServiceMapper.INSTANCE.toEditorGetResList(course.getEditorList());

        return CourseDetailGetRes.builder()
                .courseId(course.getCourseId())
                .courseName(course.getCourseName())
                .editorCnt(editorGetResList.size())
                .pinCnt(pinResList.size())
                .editorGetResList(editorGetResList)
                .pinResList(pinResList)
                .build();
    }

    @Transactional(readOnly = true)
    public CourseGetResList getAllCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Editor> editors = editorRepository.findAllByUserOrderByCreateTimestampDesc(user, pageable);

        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors.getContent()))
                .isLast(editors.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public CourseGetResList getOwnedCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Editor> editors =
                editorRepository.findAllByUserAndRoleOrderByCreateTimestampDesc(user, Role.OWNER, pageable);

        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors.getContent()))
                .isLast(editors.isLast())
                .build();
    }

    @Transactional(readOnly = true)
    public CourseGetResList getMemberCourse(Long userId, int page, int limit) {
        User user = getUserByUserId(userId);
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<Editor> editors =
                editorRepository.findAllByUserAndRoleOrderByCreateTimestampDesc(
                        user, Role.MEMBER, pageable);

        return CourseGetResList.builder()
                .courseGetResList(CourseServiceMapper.INSTANCE.toCourseGetResList(editors.getContent()))
                .isLast(editors.isLast())
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

    private void isExceedEditorLimit(Course course) {
        Long editorCnt = editorRepository.countByCourse(course);
        EditorValidator.checkIsExceedEditorLimit(editorCnt);
    }
}
