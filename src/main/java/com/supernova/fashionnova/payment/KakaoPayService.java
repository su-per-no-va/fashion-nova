package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.order.OrderDetail;
import com.supernova.fashionnova.order.OrderDetailRepository;
import com.supernova.fashionnova.order.OrderStatus;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.payment.dto.KakaoPayApproveResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayRefundResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

  private final KakaoPayConfig kakaoPayConfig;
  private final UserRepository userRepository;
  private final OrdersRepository ordersRepository;
  private final OrderDetailRepository orderDetailRepository;

  //카카오페이 요청 양식
  public KakaoPayReadyResponseDto kakaoPayReady(KakaoPayReadyRequestDto readyRequestDto, User user, Long orderId) {
    Order order = ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", kakaoPayConfig.cid);
    parameters.add("parent_order_id", String.valueOf(order.getId()));
    parameters.add("parent_user_id", String.valueOf(user.getId()));
    parameters.add("item_name", order.getOrderName());
    parameters.add("quantity", String.valueOf(order.getCount()));
    parameters.add("total_amount", String.valueOf(order.getTotalPrice()));
    parameters.add("tax_free_amount", String.valueOf(readyRequestDto.getTaxFreeAmount()));
    parameters.add("approval_url", "http://localhost:8080/payments/success/"+user.getId());
    parameters.add("cancel_url", "http://localhost:8080/payments/cancle");
    parameters.add("fail_url", "http://localhost:8080/payments/fail");

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    KakaoPayReadyResponseDto response = restTemplate.postForObject(
        "https://open-api.kakaopay.com/online/v1/payment/ready",
        requestEntity,
        KakaoPayReadyResponseDto.class);

    //카카오페이에서 tid를 응답으로 못 받았을 때
    userRepository.findByTid(user.getTid()).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_TID));

    user.updateTid(response.getTid());
    userRepository.save(user);
    return response;
  }

  private HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    String auth = "SECRET_KEY " + kakaoPayConfig.getSecretKey();
    headers.set("Authorization", auth);
    headers.set("Content_Type", "application/json");
    return headers;
  }

  public KakaoPayApproveResponseDto kakaoPayApprove(String pgToken,
      Long userId) {
    User user=userRepository.findById(userId).orElseThrow();

    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", kakaoPayConfig.cid);
    parameters.add("tid", user.getTid());
    parameters.add("pg_token", pgToken);


    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    return restTemplate.postForObject(
        "https://open-api.kakaopay.com/online/v1/payment/approve",
        requestEntity,
        KakaoPayApproveResponseDto.class);
  }

  public KakaoPayRefundResponseDto kakaoPayRefund(KakaoPayRefundRequestDto requestDto, User user, Long orderId) {
    //취소하려는 주문이 없음(이미 취소된 주문)
    Order order = ordersRepository.findById(orderId).orElseThrow(
        ()-> new CustomException(ErrorType.NOT_FOUND_ORDER));
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", kakaoPayConfig.cid);
    parameters.add("tid", user.getTid());
    parameters.add("cancle_amount", String.valueOf(requestDto.getCancleAmount()));
    parameters.add("cancle_tax_free_amount", String.valueOf(requestDto.getCancleTaxFreeAmount()));

    HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters,
        this.getHeaders());

    RestTemplate restTemplate = new RestTemplate();

    KakaoPayRefundResponseDto refundResponseDto = restTemplate.postForObject(
        "https://open-api.kakaopay.com/online/v1/payment/cancel",
        requestEntity,
        KakaoPayRefundResponseDto.class);
    //환불 후 주문상태 '환불'로 바꾸기
    order.setOrderStatus(OrderStatus.REFUND);
    return refundResponseDto;
  }
}