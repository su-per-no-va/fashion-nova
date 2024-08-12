package com.supernova.fashionnova.admin.dto;

import com.supernova.fashionnova.domain.address.dto.AddressResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserGrade;
import com.supernova.fashionnova.domain.user.UserRole;
import com.supernova.fashionnova.domain.user.UserStatus;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {

    private final Long userId;
    private final String userName;
    private final String name;
    private final String email;
    private final UserRole userRole;
    private final UserGrade userGrade;
    private final UserStatus status;
    private final LocalDateTime createdAt;
    private final List<AddressResponseDto> addressList;
    private final List<WarnResponseDto> warnList;
    private final Long totalPrice;
    private final Long recentOrderId;

    public UserProfileResponseDto(
        User user,
        List<WarnResponseDto> warnList,
        List<AddressResponseDto> addressList,
        Long totalPrice,
        Long recentOrderId) {

        this.userId = user.getId();
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
        this.userGrade = user.getUserGrade();
        this.addressList = addressList;
        this.warnList = warnList;
        this.status = user.getUserStatus();
        this.createdAt = user.getCreatedAt();
        this.totalPrice = totalPrice;
        this.recentOrderId = recentOrderId;
    }

}
