package com.supernova.fashionnova.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApprovedCancelAmount {
  private int total; //이번 요청으로 취소된 전체 금액
  private int taxFree; //이번 요청으로 취소된 비과세 금액
}
