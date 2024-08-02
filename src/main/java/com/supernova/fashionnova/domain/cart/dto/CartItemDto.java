package com.supernova.fashionnova.domain.cart.dto;

import lombok.Getter;

@Getter
public class CartItemDto {

    private String product;

    private Long price;

    private int count;

    private String size;

    private String color;

    public CartItemDto(String product, Long price, int count, String size, String color) {
        this.product = product;
        this.price = price;
        this.count = count;
        this.size = size;
        this.color = color;
    }
}