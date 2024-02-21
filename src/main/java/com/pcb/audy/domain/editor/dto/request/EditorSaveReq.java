package com.pcb.audy.domain.editor.dto.request;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EditorSaveReq {
    private Long userId;
    private String key;

    @Builder
    private EditorSaveReq(String key) {
        this.key = key;
    }
}
