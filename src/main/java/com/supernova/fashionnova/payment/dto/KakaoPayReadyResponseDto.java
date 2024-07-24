package com.supernova.fashionnova.payment.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 결제 요청 시 카카오에게 받음
 */
@Getter
@RequiredArgsConstructor
public class KakaoPayReadyResponseDto {

  private String tid; //결제 고유 번호 - 승인/취소가 동일한 결제번호
  private LocalDateTime nextRedirectPcUrl;  //요청한 클라이언트가 pc웹일 경우, 카카오톡 결제 페이지 redirectUrl
  private LocalDateTime createdAt; //결제 준비 요청 시간
}
