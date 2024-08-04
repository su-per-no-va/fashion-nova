package com.supernova.fashionnova.domain.order.dto;

import lombok.Getter;

@Getter
public class OrderDetailResponseDto {
  private Long id;
  private int count;
  private String productName;
  private Long price;

  public OrderDetailResponseDto(Long id, int count, String productName, Long price) {
    this.id = id;
    this.count = count;
    this.productName = productName;
    this.price = price;
  }
}
