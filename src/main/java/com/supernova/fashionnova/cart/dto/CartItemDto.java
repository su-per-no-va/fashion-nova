package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartItemDto {

    private  Long productDetailId;

    private String product;

    private Long price;

    private int count;

    private String size;

    private String color;

    private String imageUrl;

    public CartItemDto(String product, Long price, int count, String size, String color,String imageUrl,Long productDetailId) {
        this.product = product;
        this.price = price;
        this.count = count;
        this.size = size;
        this.color = color;
        this.imageUrl = imageUrl;
        this.productDetailId = productDetailId;
    }
}


