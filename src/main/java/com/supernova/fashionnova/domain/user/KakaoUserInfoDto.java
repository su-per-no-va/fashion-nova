package com.supernova.fashionnova.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoUserInfoDto {

    private Long id;
    private String username;
    private String email;

    public KakaoUserInfoDto(Long id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

}
