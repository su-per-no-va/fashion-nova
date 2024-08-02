package com.supernova.fashionnova.order.dto;

import com.supernova.fashionnova.address.DeliveryStatus;
import com.supernova.fashionnova.order.OrderDetail;
import com.supernova.fashionnova.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class AllOrderResponseDto {

  private Long orderId;
  private int totalPrice;
  private int cost;
  private int discount;
  private int usedMileage;
  private String address;
  private LocalDateTime createdAt;
  private DeliveryStatus deliveryStatus;
  private OrderStatus orderStatus;
  private List<OrderDetail> orderDetailList;
  private String orderName;
  private int cartCount;

  public AllOrderResponseDto(Long orderId, OrderStatus orderStatus, String address, int cost, DeliveryStatus deliveryStatus, int discount, int totalPrice, int usedMileage, LocalDateTime createdAt, List<OrderDetail> savedOrderDetailList, String orderName, int cartCount) {
    this.orderStatus = orderStatus;
    this.address = address;
    this.cost = cost;
    this.deliveryStatus = deliveryStatus;
    this.discount = discount;
    this.totalPrice = totalPrice;
    this.usedMileage = usedMileage;
    this.createdAt = createdAt;
    this.orderId = orderId;
    this.orderDetailList = savedOrderDetailList;
    this.orderName = orderName;
    this.cartCount = cartCount;
  }
}
