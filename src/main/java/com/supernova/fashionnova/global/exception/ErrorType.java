package com.supernova.fashionnova.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // USER
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST,"중복된 ID 입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이름입니다."),
    BAD_REQUEST_USER_STATUS_NON(HttpStatus.BAD_REQUEST,"탈퇴한 회원입니다."),
    BAD_REQUEST_USER_STATUS_BLOCK(HttpStatus.BAD_REQUEST,"차단된 회원입니다."),
    INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레쉬토큰을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;
}
