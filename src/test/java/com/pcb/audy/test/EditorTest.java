package com.pcb.audy.test;

import static com.pcb.audy.test.UserTest.TEST_USER;

import com.pcb.audy.domain.editor.entity.Editor;
import com.pcb.audy.global.meta.Role;

public interface EditorTest extends CourseTest {

    Editor TEST_EDITOR_ADMIN =
            Editor.builder().course(TEST_COURSE).user(TEST_USER).role(Role.OWNER).build();

    Editor TEST_EDITOR_MEMBER =
            Editor.builder().course(TEST_SECOND_COURSE).user(TEST_USER).role(Role.MEMBER).build();
}
