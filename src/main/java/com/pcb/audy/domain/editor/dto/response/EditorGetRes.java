package com.pcb.audy.domain.editor.dto.response;

import com.pcb.audy.global.meta.Role;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorGetRes {
    private Long userId;
    private String userName;
    private Role role;
    private String imageUrl;

    @Builder
    private EditorGetRes(Long userId, String userName, Role role, String imageUrl) {
        this.userId = userId;
        this.userName = userName;
        this.role = role;
        this.imageUrl = imageUrl;
    }
}
