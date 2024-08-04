package com.supernova.fashionnova.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class KakaoPayReadyRequestDto {

  private int taxFreeAmount; // 상품 비과세 금액
}
