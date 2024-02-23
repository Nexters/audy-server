package com.pcb.audy.domain.editor.dto.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorDeleteReq {
    private Long userId;
    private Long courseId;
    private Long selectedUserId;

    @Builder
    private EditorDeleteReq(Long userId, Long courseId, Long selectedUserId) {
        this.userId = userId;
        this.courseId = courseId;
        this.selectedUserId = selectedUserId;
    }
}
