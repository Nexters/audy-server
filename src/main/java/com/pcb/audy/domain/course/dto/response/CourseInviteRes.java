package com.pcb.audy.domain.course.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CourseInviteRes {

    private String url;

    @Builder
    private CourseInviteRes(String url) {
        this.url = url;
    }
}
