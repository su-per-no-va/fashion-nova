package com.supernova.fashionnova.payment.dto;

import com.supernova.fashionnova.payment.Amount;
import com.supernova.fashionnova.payment.ApprovedCancelAmount;
import com.supernova.fashionnova.payment.Status;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
  private ApprovedCancelAmount canceledAmount; //이번 요청으로 취소된 금액
  private int quantity; //수량
  private LocalDateTime createdAt; //결제 준비 요청 시각
  private LocalDateTime approvedAt; //결제 승인 시각
  private LocalDateTime canceledAt; //결제 취소 시각
}
