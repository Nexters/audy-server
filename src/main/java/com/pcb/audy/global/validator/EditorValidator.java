package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.NOT_ADMIN_COURSE;

import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Role;

public class EditorValidator {

    public static void checkIsAdminUser(Editor editor) {
        if (!editor.getRole().equals(Role.OWNER)) {
            throw new GlobalException(NOT_ADMIN_COURSE);
        }
    }
}
