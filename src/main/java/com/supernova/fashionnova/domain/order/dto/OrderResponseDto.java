package com.supernova.fashionnova.domain.order.dto;

import com.supernova.fashionnova.domain.delivery.DeliveryStatus;
import com.supernova.fashionnova.domain.order.OrderStatus;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderResponseDto {

    private Long orderId;
    private Long totalPrice;
    private Long cost;
    private int discount;
    private Long usedMileage;
    private String address;
    private LocalDateTime createdAt;
    private DeliveryStatus deliveryStatus;
    private OrderStatus orderStatus;

    public OrderResponseDto(Long orderId, OrderStatus orderStatus, String address, Long cost, DeliveryStatus deliveryStatus, int discount, Long totalPrice, Long usedMileage, LocalDateTime createdAt) {
        this.orderId = orderId;
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
