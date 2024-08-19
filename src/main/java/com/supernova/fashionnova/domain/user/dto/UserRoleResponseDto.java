package com.supernova.fashionnova.domain.user.dto;

import com.supernova.fashionnova.domain.user.UserRole;
import lombok.Getter;

@Getter
public class UserRoleResponseDto {

    private final UserRole userRole;

    public UserRoleResponseDto(UserRole userRole) {
        this.userRole = userRole;
    }

}
