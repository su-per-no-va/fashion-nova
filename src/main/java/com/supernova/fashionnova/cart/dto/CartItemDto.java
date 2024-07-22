package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartItemDto {

    private String product;

    private int price;

    private int count;

    private String size;

    private String color;

    public CartItemDto(String product, int price, int count, String size, String color) {
        this.product = product;
        this.price = price;
        this.count = count;
        this.size = size;
        this.color = color;
    }
}