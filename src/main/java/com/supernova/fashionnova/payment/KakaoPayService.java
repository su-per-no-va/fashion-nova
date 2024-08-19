package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.domain.cart.Cart;
import com.supernova.fashionnova.domain.cart.CartRepository;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.order.OrderStatus;
import com.supernova.fashionnova.domain.order.OrdersRepository;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRepository;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.payment.dto.KakaoPayApproveResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundRequestDto;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KakaoPayService {

  private final KakaoPayConfig kakaoPayConfig;
  private final UserRepository userRepository;
  private final OrdersRepository ordersRepository;
  private final CartRepository cartRepository;
  @Value("${URL}")
  private String url;

  //카카오페이 요청 양식
  public KakaoPayReadyResponseDto kakaoPayReady(User user, Long orderId, Long couponId) {
    //장바구니가 비워져있으면 결제 시도를 막음(개발자가 이런 짓을 할 수 있음)
    List<Cart> cartList = cartRepository.findAllByUserId(user.getId());
    if (cartList.isEmpty()) {
      throw new CustomException(ErrorType.CART_EMPTY);
    }

    Order order = ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", kakaoPayConfig.cid);
    parameters.put("partner_order_id", String.valueOf(order.getId()));
    parameters.put("partner_user_id", String.valueOf(user.getId()));
    parameters.put("item_name", order.getOrderName());
    parameters.put("quantity", String.valueOf(order.getCount()));
    parameters.put("total_amount", String.valueOf(order.getTotalPrice()));
    parameters.put("tax_free_amount",String.valueOf(order.getTotalPrice()));
    String approvalUrl = url + "/payments/success/" + order.getId() + "/" + user.getId() +
        "?couponId=" + (couponId != null ? String.valueOf(couponId) : "");
    parameters.put("approval_url", approvalUrl);
    parameters.put("cancel_url",  url + "/payments/cancel" + "/" + user.getId());
    parameters.put("fail_url",  url + "/payments/fail");

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<KakaoPayReadyResponseDto> response = restTemplate.postForEntity(
        "https://open-api.kakaopay.com/online/v1/payment/ready",
        requestEntity,
        KakaoPayReadyResponseDto.class);
    log.info(String.valueOf(response.getBody().getTid()));

    //order
    order.updateTid(response.getBody().getTid());

    ordersRepository.save(order);
    //카카오페이에서 tid를 응답으로 못 받았을 때
    ordersRepository.findByTid(order.getTid()).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_TID));

    return response.getBody();
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String auth = "SECRET_KEY " + kakaoPayConfig.getSecretKey();
    headers.set("Authorization", auth);
    headers.set("Content-Type", "application/json; charset=utf-8");
    return headers;
  }

  @Transactional
  public KakaoPayApproveResponseDto kakaoPayApprove(String pgToken,
      Long userId, Long orderId) {
    User user=userRepository.findById(userId).orElseThrow();
    Order order=ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", kakaoPayConfig.cid);
    parameters.put("tid", order.getTid());
    parameters.put("pg_token", pgToken);
    parameters.put("partner_order_id", String.valueOf(orderId));
    parameters.put("partner_user_id", String.valueOf(user.getId()));

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();
    ResponseEntity<KakaoPayApproveResponseDto> response = restTemplate.postForEntity(
        "https://open-api.kakaopay.com/online/v1/payment/approve",
        requestEntity,
        KakaoPayApproveResponseDto.class);
    return response.getBody();
  }

  public KakaoPayRefundResponseDto kakaoPayRefund(KakaoPayRefundRequestDto requestDto, User user, Long orderId) {
    //취소하려는 주문이 없음(이미 취소된 주문)
    Order order = ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
    //본인의 주문만 환불 가능
    if(!order.getUser().getId().equals(user.getId())) {
      throw new CustomException(ErrorType.DENIED_PERMISSION);
    }
    Map<String, String> parameters = new HashMap<>();
    parameters.put("cid", kakaoPayConfig.cid);
    parameters.put("tid", order.getTid());
    parameters.put("cancel_amount", String.valueOf(requestDto.getCancelAmount()));
    parameters.put("cancel_tax_free_amount", String.valueOf(requestDto.getCancelTaxFreeAmount()));

    HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    ResponseEntity<KakaoPayRefundResponseDto> refundResponseDto = restTemplate.postForEntity(
        "https://open-api.kakaopay.com/online/v1/payment/cancel",
        requestEntity,
        KakaoPayRefundResponseDto.class);
    //환불 후 주문상태 '환불'로 바꾸기
    order.setOrderStatus(OrderStatus.REFUND);
    return refundResponseDto.getBody();
  }

  public void deleteFail(Long userId) {
    List<Order> order = ordersRepository.findAllByUserId(userId);
    if (order.isEmpty()) {
      throw new CustomException(ErrorType.NOT_FOUND_ORDER);
    }
    for (Order filterOrder : order) {
      if (filterOrder.getOrderStatus().equals(OrderStatus.PROGRESS)) {
        ordersRepository.delete(filterOrder);
      }
      }
    }
}