package com.supernova.fashionnova.domain.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartItemDto {

    private final List<String> sizes;
    private final List<String> colors;
    private final String product;
    private final Long price;
    private final int count;
    private final String size;
    private final String color;
    private final String imageUrl;
    private final Long productDetailId;

    public CartItemDto(String product, Long price, int count, String size, String color,
        String imageUrl, Long productDetailId, List<String> colors, List<String> sizes) {
        this.product = product;
        this.price = price;
        this.count = count;
        this.size = size;
        this.color = color;
        this.imageUrl = imageUrl;
        this.productDetailId = productDetailId;
        this.colors = colors;
        this.sizes = sizes;
    }

}
