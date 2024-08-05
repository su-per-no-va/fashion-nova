package com.supernova.fashionnova.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatus {
    MEMBER,
    NON_MEMBER,
    BLOCKED_MEMBER
}
