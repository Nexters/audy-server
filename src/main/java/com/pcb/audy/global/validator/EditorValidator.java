package com.pcb.audy.global.validator;

import static com.pcb.audy.global.response.ResultCode.*;

import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.global.exception.GlobalException;
import com.pcb.audy.global.meta.Role;

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

    private static boolean isExistEditor(Editor editor) {
        return editor != null;
    }

    private static boolean isAdminEditor(Editor editor) {
        return Role.OWNER.equals(editor.getRole());
    }
}
