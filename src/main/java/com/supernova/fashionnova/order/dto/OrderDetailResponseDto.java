package com.supernova.fashionnova.order.dto;

import com.supernova.fashionnova.order.OrderDetail;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class OrderDetailResponseDto {

  private Long id;
  private int count;
  private String productName;
  private int price;

  public OrderDetailResponseDto(Long id, int count, String productName, int price) {
    this.id = id;
    this.count = count;
    this.productName = productName;
    this.price = price;
  }
}
