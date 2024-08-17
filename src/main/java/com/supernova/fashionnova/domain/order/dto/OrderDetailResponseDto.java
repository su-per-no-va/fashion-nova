package com.supernova.fashionnova.domain.order.dto;

import com.supernova.fashionnova.domain.order.OrderDetail;
import com.supernova.fashionnova.domain.order.OrderStatus;
import lombok.Getter;

@Getter
public class OrderDetailResponseDto {

    private final Long id;
    private int count;
    private String productName;
    private Long price;
    private String size;
    private String color;
    private OrderStatus orderStatus;
    private String productImageUrl;

    public OrderDetailResponseDto(
        Long id,
        int count,
        String productName,
        Long price,
        String size,
        String color,
        OrderStatus orderStatus,
        String productImageUrl) {

        this.id = id;
        this.count = count;
        this.productName = productName;
        this.price = price;
        this.size = size;
        this.color = color;
        this.orderStatus = orderStatus;
        this.productImageUrl = productImageUrl;
    }

    public OrderDetailResponseDto(OrderDetail orderDetail) {

        if (orderDetail != null) {
            this.id = orderDetail.getId();
            this.count = orderDetail.getCount();
            this.productName = orderDetail.getProductName();
            this.price = orderDetail.getPrice();
        } else {
            this.id = null;
        }

    }

}
