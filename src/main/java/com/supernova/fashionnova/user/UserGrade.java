package com.supernova.fashionnova.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserGrade {
    BRONZE("BRONZE"),
    SILVER("SILVER"),
    GOLD("GOLD"),
    ;

    private final String userGradeName;
}
