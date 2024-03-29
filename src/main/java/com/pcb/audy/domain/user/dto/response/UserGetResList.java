package com.pcb.audy.domain.user.dto.response;

import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGetResList {
    private List<SocketUserGetRes> users;
    private int total;

    @Builder
    private UserGetResList(List<SocketUserGetRes> users, int total) {
        this.users = users;
        this.total = total;
    }
}
