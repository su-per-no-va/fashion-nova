package com.supernova.fashionnova.domain.order.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class OrderRequestDto {

    private Long totalPrice;
    private Long cost;
    private int discount;
    private Long usedMileage;
    @NotBlank
    private String address;

    public OrderRequestDto(Long cost, int discount, Long usedMileage, String address,
        Long totalPrice) {
        this.cost = cost;
        this.discount = discount;
        this.usedMileage = usedMileage;
        this.address = address;
        this.totalPrice = totalPrice;
    }

}
