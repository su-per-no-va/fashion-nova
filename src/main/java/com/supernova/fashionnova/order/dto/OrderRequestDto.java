package com.supernova.fashionnova.order.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

  private int totalPrice;
  private int cost;
  private int discount;
  private int usedMileage;
  private String address;
}
