package com.supernova.fashionnova.admin.dto;

import com.supernova.fashionnova.domain.address.Address;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserGrade;
import com.supernova.fashionnova.domain.user.UserRole;
import com.supernova.fashionnova.domain.user.UserStatus;
import com.supernova.fashionnova.domain.warn.Warn;
import java.util.List;
import lombok.Getter;

@Getter
public class UserProfileResponseDto {
    private final Long userId;
    private  final String userName;
    private final String name;
    private final String email;
    private final UserRole userRole;
    private final UserGrade userGrade;
    private final List<Address> addressList;
    private final List<Warn> warnList;
    private final UserStatus status;

    public UserProfileResponseDto(User user, List<Warn> warnList) {
        this.userId = user.getId();
        this.userName = user.getUserName();
        this.name = user.getName();
        this.email = user.getEmail();
        this.userRole = user.getUserRole();
        this.userGrade = user.getUserGrade();
        this.addressList = user.getAddressList();
        this.warnList = warnList;
        this.status = user.getUserStatus();
    }
}
