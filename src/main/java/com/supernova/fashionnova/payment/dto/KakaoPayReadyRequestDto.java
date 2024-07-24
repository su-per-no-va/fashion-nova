package com.supernova.fashionnova.payment.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class KakaoPayReadyRequestDto {

  private String cid; //가맹점 코드
  private String parentOrderId; //가맹점 주문 번호
  private String parentUserId; //가맹점 회원 id
  private String itemName; //상품명
  private int quantity; //상품 수량
  private int totalAmount;  //총 금액
  private int taxFreeAmount; // 상품 비과세 금액
  private int cost;  //정상 가격
  private int discount; //할인 금액
  private int usedMileage; //사용할 마일리지

}
