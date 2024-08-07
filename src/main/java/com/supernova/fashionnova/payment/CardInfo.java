package com.supernova.fashionnova.payment;

public class CardInfo {
  private String bin; //카드 BIN
  private String cardType; //카드 타입
  private String installMonth; //카드 타입
  private String approvedId; //카드사 승인 번호
  private String cardMid; // 카드사 가맹점 번호
  private String interestFreeInstall; //무이자 할부 여부
  private String installmentType; //할부 유형
  //CARD_INSTALLMENT: 업종 무이자
  //SHARE_INSTALLMENT: 분담 무이자
}
