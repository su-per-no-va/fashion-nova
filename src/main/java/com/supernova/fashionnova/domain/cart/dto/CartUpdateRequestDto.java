package com.supernova.fashionnova.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    private final Long productDetailId;
    private final int count;
    private final String size;
    private final String color;

    public CartUpdateRequestDto(Long productDetailId, int count, String size, String color) {
        this.productDetailId = productDetailId;
        this.count = count;
        this.size = size;
        this.color = color;
    }

}
