package com.supernova.fashionnova.payment.dto;

import com.supernova.fashionnova.payment.Amount;
import com.supernova.fashionnova.payment.ApprovedCancelAmount;
import com.supernova.fashionnova.payment.Status;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoPayRefundResponseDto {

  private String aid; //요청 고유번호
  private String tid; //결제 고유번호
  private Status status; //결제 상태
  private Amount amount; //결제 금액
  private ApprovedCancelAmount canceled_amount; //이번 요청으로 취소된 금액
  private String item_name; //상품 이름, 최대 10자
  private int quantity; //수량
  private LocalDateTime canceled_at; //결제 취소 시각
}
