package com.supernova.fashionnova.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoPayRefundRequestDto {

  private int cancelAmount; // 취소 금액
  private int cancelTaxFreeAmount; //취소 비과세 금액
}
