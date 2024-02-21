package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.*;

import com.pcb.audy.domain.course.dto.request.CourseInviteRedisReq;
import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Role;
import com.pcb.audy.global.response.ResultCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class EditorValidator {

    public static void validate(Editor editor) {
        if (!isExistEditor(editor)) {
            throw new GlobalException(NOT_FOUND_EDITOR);
        }
    }

    public static void checkIsAdminUser(Editor editor) {
        if (!isExistEditor(editor)) {
            throw new GlobalException(NOT_FOUND_EDITOR);
        }

        if (!isAdminEditor(editor)) {
            throw new GlobalException(NOT_ADMIN_COURSE);
        }
    }

    public static void checkAlreadyExist(Editor editor) {
        if (isExistEditor(editor)) {
            throw new GlobalException(ALREADY_EXIST_EDITOR);
        }
    }

    private static boolean isExistKey(String key) {
        return StringUtils.hasText(key);
    }

    private static boolean isExistEditor(Editor editor) {
        return editor != null;
    }

    private static boolean isAdminEditor(Editor editor) {
        return Role.OWNER.equals(editor.getRole());
    }

    private static boolean isMatchedKey(String key, String findKey) {
        return key.equals(findKey);
    }

    public static void validateObject(
            CourseInviteRedisReq courseInviteRedisReq, CourseInviteRedisReq findByKey) {
        if (!courseInviteRedisReq.equals(findByKey)) {
            throw new GlobalException(ResultCode.NOT_VALID_KEY);
        }
    }
}
