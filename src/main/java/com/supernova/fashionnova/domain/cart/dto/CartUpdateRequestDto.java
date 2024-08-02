package com.supernova.fashionnova.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    private Long productDetailId;

    private int count;

    private String size;

    private String color;

    public CartUpdateRequestDto(Long productDetailId, int count, String size, String color) {
        this.productDetailId = productDetailId;
        this.count = count;
        this.size = size;
        this.color = color;
    }

}
