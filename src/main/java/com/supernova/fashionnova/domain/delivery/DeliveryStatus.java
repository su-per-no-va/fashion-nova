package com.supernova.fashionnova.domain.delivery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum DeliveryStatus {
    BEFORE("배송 준비 중"),
    PROGRESS("배송 중"),
    COMPLETED("배송 완료");

    private final String msg;
}
