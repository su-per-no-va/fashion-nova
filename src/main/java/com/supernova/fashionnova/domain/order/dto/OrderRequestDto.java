package com.supernova.fashionnova.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

  private int totalPrice;
  private int cost;
  private int discount;
  private int usedMileage;
  @NotBlank
  private String address;
}

