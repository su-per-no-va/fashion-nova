package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.domain.cart.CartService;
import com.supernova.fashionnova.domain.coupon.CouponService;
import com.supernova.fashionnova.domain.mileage.MileageService;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderService;
import com.supernova.fashionnova.domain.product.ProductService;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserService;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundRequestDto;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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
  private final ProductService productService;
  private final CartService cartService;
  private final MileageService mileageService;
  private final UserService userService;
  private final CouponService couponService;

  /**
   * 결제 요청
   * */
  @PostMapping("/ready/{orderId}")
  public KakaoPayReadyResponseDto kakaoPayReady(
      @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long orderId, @RequestParam(required = false) Long couponId)
  {
    log.info("~~~~~~~~~~~~~");
    //수량 이상의 주문 막기
    productService.checkQuantity(orderId);
    log.info("~~~~~~~~~~~~~1111");
    return kakaoPayService.kakaoPayReady(userDetails.getUser(), orderId, couponId);
  }

  @Transactional
  @GetMapping("/success/{orderId}/{userId}/{couponId}")
  public void KakaoRequestSuccess(@RequestParam("pg_token") String pgToken,
      @PathVariable Long orderId,
      @PathVariable Long userId,
      @RequestParam(required = false) Long couponId,
      HttpServletResponse response)
      throws IOException {
    User user = userService.getUserById(userId);
    Order order = orderService.getOrderById(orderId);
    kakaoPayService.kakaoPayApprove(pgToken, userId, orderId);
    //주문 성공 후 재고 차감
    productService.calculateQuantity(PayAction.BUY, order);
    //마일리지 차감
    mileageService.calculateMileage(PayAction.BUY, order, user);
    //쿠폰 사용
    couponService.calculateCoupon(PayAction.BUY, couponId);
    //주문 상태 바꾸기
    orderService.updateOrderStatus(order);
    //주문 성공 후 장바구니 비우기 실행
    cartService.clearCart(userId);
    log.info("결제 성공");
    response.sendRedirect("/payments-completed.html");
  }
/**
 * 결제 취소(사용자가 중단)
 * */
  @GetMapping("/cancel/{userId}")
  public void KakaoPayCanceled(@PathVariable Long userId, HttpServletResponse response) throws IOException {
    log.info("결제 요청 실패");
    //실패한 주문 삭제
    kakaoPayService.deleteFail(userId);
    response.sendRedirect("/payments-canceled.html");
  }

  /**
   * 결제 취소(환불)
   * */
  @PostMapping("/refund/{orderId}/{couponId}")
  public KakaoPayRefundResponseDto Refund(@RequestBody KakaoPayRefundRequestDto requestDto, @AuthenticationPrincipal UserDetailsImpl userDetails, @PathVariable Long orderId, @PathVariable Long couponId) {
    KakaoPayRefundResponseDto responseDto = kakaoPayService.kakaoPayRefund(requestDto, userDetails.getUser(), orderId);
    Order order = orderService.getOrderById(orderId);
    productService.calculateQuantity(PayAction.CANCEL, order);
    mileageService.calculateMileage(PayAction.CANCEL, order, userDetails.getUser());
    couponService.calculateCoupon(PayAction.CANCEL, couponId);
    return responseDto;
  }
}
