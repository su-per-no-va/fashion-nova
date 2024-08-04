package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.cart.CartService;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.order.OrderService;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundResponseDto;
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
  private final OrderService orderService;
  private final CartService cartService;

  /**
   * 결제 요청
   * */
  @PostMapping("/ready/{orderId}")
  public KakaoPayReadyResponseDto kakaoPayReady(@RequestBody KakaoPayReadyRequestDto kakaoPayReadyRequestDto,
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long orderId)
  {
    log.info("In kakaoPayReady, orderId:{}", orderId);
    return kakaoPayService.kakaoPayReady(kakaoPayReadyRequestDto, userDetails.getUser(), orderId);
  }

  @GetMapping("/success/{userId}")
  public void KakaoRequestSuccess(@RequestParam String pgToken,
      @PathVariable Long userId, @PathVariable Long orderId) {
    kakaoPayService.kakaoPayApprove(pgToken, userId);
    //주문 상태 바꾸기
    orderService.updateOrderStatus(orderId);
    //주문 성공 후 장바구니 비우기 실행
    cartService.clearCart(userId);
  }

  @GetMapping("/fail")
  public void KakaoRequestFail() {
    log.info("결제 요청 실패");
  }

  /**
   * 결제 진행 중 취소
   * */
  @GetMapping("/cancel")
  public void KakaoRequestCancel() {
    log.info("결제 요청 취소");
  }

  /**
   * 결제 취소(환불)
   * */
  @PostMapping("/refund")
  public KakaoPayRefundResponseDto Refund(@RequestBody KakaoPayRefundRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long orderId) {
    return kakaoPayService.kakaoPayRefund(requestDto, userDetails.getUser(), orderId);
  }
}
