package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.payment.dto.KakaoPayApproveResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Slf4j
public class KakaoPayController {
  private final KakaoPayService kakaoPayService;

  /**
   * 결제 요청
   * */
  @PostMapping("/ready")
  public KakaoPayReadyResponseDto kakaoPayReady(@RequestBody KakaoPayReadyRequestDto kakaoPayReadyRequestDto, @AuthenticationPrincipal
      UserDetailsImpl userDetails) {
    return kakaoPayService.kakaoPayReady(kakaoPayReadyRequestDto, userDetails.getUser());
  }

  @GetMapping("/success/{userId}")
  public void KakaoRequestSuccess(@RequestParam String pgToken, @PathVariable Long userId) {
    kakaoPayService.kakaoPayApprove(pgToken, userId);
    //장바구니 완성 되면 장바구니 비우기 실행
    log.info("결제 요청 성공");
  }

  @GetMapping("/fail")
  public void KakaoRequestFail() {
    log.info("결제 요청 실패");

  }

  /**
   * 결제 진행 중 취소
   * */
  @GetMapping("/cancle")
  public void KakaoRequestCancle() {
    log.info("결제 요청 취소");
  }

  /**
   * 결제 취소(환불)
   * */
//  @PostMapping("/refund")
//  public
}
