package com.supernova.fashionnova.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    MEMBER("MEMBER"),
    NON_MEMBER("NON_MEMBER"),
    BLOCKED_MEMBER("BLOCKED_MEMBER"),
    ;

    private final String UserStatusName;
}
