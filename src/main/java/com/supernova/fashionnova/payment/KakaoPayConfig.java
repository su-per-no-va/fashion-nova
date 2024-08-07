package com.supernova.fashionnova.payment;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class KakaoPayConfig {

  public final String secretKey;
  public final String cid;

  @Autowired
  public KakaoPayConfig(@Value("${kakaopay.secret-key}") String secretKey,
      @Value("${kakaopay.cid}") String cid) {
    this.secretKey = secretKey;
    this.cid = cid;
  }
}
