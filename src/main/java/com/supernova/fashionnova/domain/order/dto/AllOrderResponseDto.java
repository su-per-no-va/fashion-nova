package com.supernova.fashionnova.domain.order.dto;

import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AllOrderResponseDto {

  private Long orderId;
  private double totalPrice;
  private double cost;
  private double discount;
  private Long usedMileage;
  private String address;
  private LocalDateTime createdAt;
  private DeliveryStatus deliveryStatus;
  private OrderStatus orderStatus;
  private List<OrderDetailResponseDto> orderDetailList;
  private String orderName;
  private int cartCount;

  public AllOrderResponseDto(
      Long orderId,
      OrderStatus orderStatus,
      String address,
      Long cost,
      DeliveryStatus deliveryStatus,
      int discount,
      Long totalPrice,
      Long usedMileage,
      LocalDateTime createdAt,
      List<OrderDetail> savedOrderDetailList,
      String orderName,
      int cartCount
  ) {
    this.orderStatus = orderStatus;
    this.address = address;
    this.cost = cost;
    this.deliveryStatus = deliveryStatus;
    this.discount = discount;
    this.totalPrice = totalPrice;
    this.usedMileage = usedMileage;
    this.createdAt = createdAt;
    this.orderId = orderId;
    this.orderDetailList = savedOrderDetailList.stream()
        .map(orderDetail -> new OrderDetailResponseDto(
            orderDetail.getId(),
            orderDetail.getCount(),
            orderDetail.getProductName(),
            orderDetail.getPrice()
        ))
        .toList();
    this.orderName = orderName;
    this.cartCount = cartCount;
  }
}
