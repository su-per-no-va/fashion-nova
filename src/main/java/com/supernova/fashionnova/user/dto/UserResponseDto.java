package com.supernova.fashionnova.user.dto;

import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserGrade;
import com.supernova.fashionnova.user.UserStatus;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class UserResponseDto {

    private final String userName;
    private final String name;
    private final String email;
    private final String phone;
    private final UserStatus status;
    private final UserGrade grade;
    private final Long mileage;
    private final LocalDateTime createdAt;

    public UserResponseDto(User user) {
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.phone = user.getPhone();
        this.status = user.getUserStatus();
        this.grade = user.getUserGrade();
        this.mileage = user.getMileage();
        this.createdAt = user.getCreatedAt();
    }

}
