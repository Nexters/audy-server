package com.pcb.audy.global.meta;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Authority {
    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String value;
}
