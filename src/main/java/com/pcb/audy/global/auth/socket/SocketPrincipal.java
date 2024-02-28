package com.pcb.audy.global.auth.socket;

import com.pcb.audy.domain.user.entity.User;
import java.security.Principal;
import javax.security.auth.Subject;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocketPrincipal implements Principal {
    private User user;

    @Builder
    private SocketPrincipal(User user) {
        this.user = user;
    }

    @Override
    public String getName() {
        return user.getEmail();
    }

    @Override
    public boolean implies(Subject subject) {
        return Principal.super.implies(subject);
    }
}
