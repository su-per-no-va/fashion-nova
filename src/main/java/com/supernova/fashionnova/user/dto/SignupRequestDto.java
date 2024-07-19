package com.supernova.fashionnova.user.dto;

import lombok.Getter;

@Getter
public class SignupRequestDto {
    private String userName;
    private String password;
    private String name;
    private String email;
    private String phone;
}
