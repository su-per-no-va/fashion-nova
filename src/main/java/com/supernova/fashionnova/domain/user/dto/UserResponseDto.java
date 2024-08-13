package com.supernova.fashionnova.domain.user.dto;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserGrade;
import com.supernova.fashionnova.domain.user.UserStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final Long id;
    private final String userName;
    private final String name;
    private final String email;
    private final String phone;
    private final UserStatus status;
    private final UserGrade grade;
    private final Long mileage;
    private final LocalDateTime createdAt;
    private final int warnCount;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.status = user.getUserStatus();
        this.grade = user.getUserGrade();
        this.mileage = user.getMileage();
        this.createdAt = user.getCreatedAt();
        this.warnCount = user.getWarnList().size();
    }

}
