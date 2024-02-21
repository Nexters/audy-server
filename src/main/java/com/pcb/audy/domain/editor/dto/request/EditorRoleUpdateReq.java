package com.pcb.audy.domain.editor.dto.request;

import com.pcb.audy.global.meta.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorRoleUpdateReq {
    private Long userId;
    private Long courseId;
    private Long selectedUserId;
    private Role role;

    @Builder
    private EditorRoleUpdateReq(Long userId, Long courseId, Long selectedUserId, Role role) {
        this.userId = userId;
        this.courseId = courseId;
        this.selectedUserId = selectedUserId;
        this.role = role;
    }
}
