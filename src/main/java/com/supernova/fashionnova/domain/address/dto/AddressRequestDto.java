package com.supernova.fashionnova.domain.address.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AddressRequestDto {

    @NotBlank(message = "배송지명 입력값이 없습니다.")
    private String name;

    @NotBlank(message = "배송인 입력값이 없습니다.")
    private String recipientName;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$",
        message = "휴대폰 형식에 맞지 않습니다. 휴대폰 형식: 010-****-**** ")
    @NotBlank(message = "휴대폰 입력값이 없습니다.")
    private String recipientNumber;

    @Pattern(regexp = "^\\d{5}$", message = "우편번호는 5자리 숫자여야 합니다.")
    @NotBlank(message = "우편번호 입력값이 없습니다.")
    private String zipCode;

    @NotBlank(message = "주소 입력값이 없습니다.")
    private String address;

    private String detail;

    public AddressRequestDto(String name, String recipientName, String recipientNumber, String zipCode, String address, String detail) {
        this.name = name;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.zipCode = zipCode;
        this.address = address;
        this.detail = detail;
    }

}
