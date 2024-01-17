package com.pcb.audy.domain.editor.entity;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorId implements Serializable {
    private Long user;
    private Long course;

    @Builder
    private EditorId(Long user, Long course) {
        this.user = user;
        this.course = course;
    }
}
