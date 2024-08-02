package com.supernova.fashionnova.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoPayRefundRequestDto {

  private int cancleAmount; // 취소 금액
  private int cancleTaxFreeAmount; //취소 비과세 금액
}
