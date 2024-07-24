package com.supernova.fashionnova.payment;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.payment.dto.KakaoPayApproveResponseDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyRequestDto;
import com.supernova.fashionnova.payment.dto.KakaoPayReadyResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoPayService {

  private final KakaoPayConfig kakaoPayConfig;
  private final UserRepository userRepository;

  //카카오페이 요청 양식
  public KakaoPayReadyResponseDto kakaoPayReady(KakaoPayReadyRequestDto readyRequestDto, User user) {

    int totalAmount = calculateTotalAmount(readyRequestDto);
    MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
    parameters.add("cid", kakaoPayConfig.cid);
    parameters.add("parentOrderId", readyRequestDto.getParentOrderId());
    parameters.add("parentUserId", readyRequestDto.getParentUserId());
    parameters.add("itemName", readyRequestDto.getItemName());
    parameters.add("totalAmount", String.valueOf(totalAmount));
    parameters.add("taxFreeAmount", String.valueOf(readyRequestDto.getTaxFreeAmount()));
    parameters.add("approval_url", "http://localhost:8080/payments/success/"+user.getId());
    parameters.add("cancle_url", "http://localhost:8080/payments/cancle");
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
    headers.set("CONTENT_TYPE", "application/json");
    return headers;
  }

  private int calculateTotalAmount(KakaoPayReadyRequestDto readyRequestDto) {
    return readyRequestDto.getCost() - readyRequestDto.getDiscount()
        - readyRequestDto.getUsedMileage();
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
}
