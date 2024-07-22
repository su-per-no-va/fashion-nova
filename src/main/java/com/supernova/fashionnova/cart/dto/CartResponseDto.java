package com.supernova.fashionnova.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final List<CartItemDto> items;

    private final int totalPrice;

    public CartResponseDto(List<CartItemDto> items, int totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
