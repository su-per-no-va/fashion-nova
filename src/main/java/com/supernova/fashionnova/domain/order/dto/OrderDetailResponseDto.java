package com.supernova.fashionnova.domain.order.dto;

import com.supernova.fashionnova.domain.order.OrderDetail;
import lombok.Getter;

@Getter
public class OrderDetailResponseDto {

    private final Long id;
    private int count;
    private String productName;
    private Long price;

    public OrderDetailResponseDto(Long id, int count, String productName, Long price) {
        this.id = id;
        this.count = count;
        this.productName = productName;
        this.price = price;
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
