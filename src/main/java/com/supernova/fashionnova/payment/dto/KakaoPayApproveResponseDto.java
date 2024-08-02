package com.supernova.fashionnova.payment.dto;

import com.supernova.fashionnova.payment.Amount;
import com.supernova.fashionnova.payment.CardInfo;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class KakaoPayApproveResponseDto {

  private String aid; //요청 고유 번호 - 승인/취소가 구분된 결제번호
  private String tid; //결제 고유 번호 - 승인/취소가 동일한 결제번호
  private String cid; //가맹점 코드
  private String parentOrderId; //가맹점 주문 번호
  private String parentUserId; //가맹점 회원 id
  private String itemName; //상품명
  private int quantity; //상품 수량
  private Amount amount; //결제 금액 정보
  private CardInfo cardInfo; //결제 상세 정보, 결제 수단이 카드일 경우만 포함
  private LocalDateTime createdAt; //결제 준비 요청 시각
  private LocalDateTime approvedAt; //결제 승인 시각
}
