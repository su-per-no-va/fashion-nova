package com.supernova.fashionnova.admin.dto;

import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRole;
import com.supernova.fashionnova.domain.user.UserStatus;
import java.util.List;
import lombok.Getter;

@Getter
public class UsersCouponAndMileageResponseDto {

    private final Long id;
    private final String UserName;
    private final String name;
    private final UserRole userRole;
    private final String email;
    private final UserStatus userStatus;
    private final List<CouponResponseDto> couponResponseDtoList;
    private final Long mileage;

    public UsersCouponAndMileageResponseDto(User user) {
        this.id = user.getId();
        this.UserName = user.getUserName();
        this.name = user.getName();
        this.userRole = user.getUserRole();
        this.email = user.getEmail();
        this.userStatus = user.getUserStatus();
        this.couponResponseDtoList = user.getCouponList()
            .stream()
            .map(CouponResponseDto::new)
            .toList();
        this.mileage = user.getMileage();
    }

}
