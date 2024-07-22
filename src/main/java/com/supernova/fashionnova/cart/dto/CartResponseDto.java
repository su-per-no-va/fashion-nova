package com.supernova.fashionnova.cart.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartResponseDto {

    private List<CartItemDto> items;

    private int totalPrice;

    public CartResponseDto(List<CartItemDto> items, int totalPrice) {
        this.items = items;
        this.totalPrice = totalPrice;
    }
}
