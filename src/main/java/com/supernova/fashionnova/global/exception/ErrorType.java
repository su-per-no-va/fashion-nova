package com.supernova.fashionnova.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorType {
    // USER
    DUPLICATED_USERNAME(HttpStatus.BAD_REQUEST, "중복된 ID 입니다."),
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다."),
    BAD_REQUEST_USER_STATUS_NON(HttpStatus.BAD_REQUEST, "탈퇴한 회원입니다."),
    BAD_REQUEST_USER_STATUS_BLOCK(HttpStatus.BAD_REQUEST, "차단된 회원입니다."),
    INVALID_ACCOUNT_ID(HttpStatus.UNAUTHORIZED, "아이디가 일치하지 않습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND, "토큰을 찾을 수 없습니다."),
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "유저를 찾을 수 없습니다."),
    NOT_FOUND_TID(HttpStatus.NOT_FOUND, "tid를 찾을 수 없습니다."),
    NOT_FOUND_REFRESH_TOKEN(HttpStatus.NOT_FOUND, "리프레쉬토큰을 찾을 수 없습니다."),
    // ADDRESS
    NOT_FOUND_ADDRESS(HttpStatus.NOT_FOUND, "배송지를 찾을 수 없습니다."),
    INVALID_ADDRESS(HttpStatus.BAD_REQUEST, "유저의 배송지가 아닙니다."),

    // WISH
    NOT_FOUND_WISH(HttpStatus.NOT_FOUND, "위시리시트를 찾을 수 없습니다."),
    INVALID_WISH(HttpStatus.UNAUTHORIZED, "유저의 위시리스트가 아닙니다."),
    BAD_REQUEST_WISH_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 위시입니다."),

    // QUESTION
    NOT_FOUND_QUESTION(HttpStatus.NOT_FOUND, "문의를 찾을 수 없습니다."),

    // PRODUCT
    NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
    OUT_OF_STOCK(HttpStatus.BAD_REQUEST, "품절된 상품입니다."),
    NOT_ORDERED_PRODUCT(HttpStatus.BAD_REQUEST, "구매하지 않은 상품입니다."),
    DUPLICATED_DETAIL(HttpStatus.BAD_REQUEST, "중복된 상품 디테일입니다."),
    NOT_FOUND_PRODUCT_DETAIL(HttpStatus.BAD_REQUEST, "상품 상세를 찾을 수 없습니다."),

    // CART
    CART_EMPTY(HttpStatus.BAD_REQUEST, "장바구니에 상품이 존재하지 않습니다."),

    // REVIEW
    NOT_FOUND_REVIEW(HttpStatus.BAD_REQUEST, "리뷰가 존재하지 않습니다."),

    // WARN
    NOT_FOUND_WARN(HttpStatus.NOT_FOUND, "경고를 찾을 수 없습니다."),

    // UPLOAD
    UPLOAD_REVIEW(HttpStatus.INTERNAL_SERVER_ERROR, "리뷰 사진 등록중 실패"),

    // ORDER
    NOT_FOUND_ORDER(HttpStatus.BAD_REQUEST, "주문이 존재하지 않습니다."),

    // ORDER_DETAIL
    DENIED_PERMISSION(HttpStatus.UNAUTHORIZED, "권한 없음");

    private final HttpStatus httpStatus;
    private final String message;
}
