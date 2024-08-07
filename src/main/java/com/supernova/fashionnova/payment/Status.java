package com.supernova.fashionnova.payment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Status {
  READY("READY", "결제 요청"),
  SEND_TMS("SEND_TMS", "결제 요청 메시지(TMS) 발송 완료"),
  OPEN_PAYMENT("OPEN_PAYMENT", "사용자가 카카오페이 결제 화면 진입"),
  SELECT_METHOD("SELECT_METHOD", "결제 수단 선택, 인증 완료"),
  ARS_WAITING("ARS_WAITING", "ARS 인증 진행 중"),
  AUTH_PASSWORD("AUTH_PASSWORD", "비밀번호 인증 완료"),
  ISSUED_SID("ISSUED_SID", "SID 발급 완료, 정기 결제 시 SID만 발급 한 경우"),
  SUCCESS_PAYMENT("SUCCESS_PAYMENT", "결제 완료"),
  CANCEL_PAYMENT("CANCEL_PAYMENT", "결제된 금액 모두 취소"),
  FAIL_AUTH_PASSWORD("FAIL_AUTH_PASSWORD", "사용자 비밀번호 인증 실패"),
  QUIT_PAYMENT("QUIT_PAYMENT", "사용자가 결제 중단"),
  FAIL_PAYMENT("FAIL_PAYMENT", "결제 승인 실패"),
  PART_CANCEL_PAYMENT("PART_CANCEL_PAYMENT", "부분 취소");
  private final String value;
  private final String msg;

}
