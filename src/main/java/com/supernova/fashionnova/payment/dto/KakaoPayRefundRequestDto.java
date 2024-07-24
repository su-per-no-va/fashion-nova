package com.supernova.fashionnova.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoPayRefundRequestDto {

  private String cid; //가맹점 코드
  private String tid; //결제 고유번호
  private int cancleAmount; // 취소 금액
  private int cancleTaxFreeAmount; //취소 비과세 금액
}
