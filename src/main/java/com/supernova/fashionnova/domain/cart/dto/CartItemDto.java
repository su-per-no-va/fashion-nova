package com.supernova.fashionnova.domain.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartItemDto {

    private List<String> sizes;
    private List<String> colors;
    private String product;
    private Long price;
    private int count;
    private String size;
    private String color;
    private String imageUrl;
    private Long productDetailId;

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
