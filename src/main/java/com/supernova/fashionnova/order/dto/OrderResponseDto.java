package com.supernova.fashionnova.order.dto;

import com.supernova.fashionnova.address.DeliveryStatus;
import com.supernova.fashionnova.order.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

  private Long orderId;
  private int totalPrice;
  private int cost;
  private int discount;
  private int usedMileage;
  private String address;
  private LocalDateTime createdAt;
  private DeliveryStatus deliveryStatus;
  private OrderStatus orderStatus;

  public OrderResponseDto(Long orderId, OrderStatus orderStatus, String address, int cost, DeliveryStatus deliveryStatus, int discount, int totalPrice, int usedMileage, LocalDateTime createdAt) {
    this.orderStatus = orderStatus;
    this.address = address;
    this.cost = cost;
    this.deliveryStatus = deliveryStatus;
    this.discount = discount;
    this.totalPrice = totalPrice;
    this.usedMileage = usedMileage;
    this.createdAt = createdAt;
  }
}
