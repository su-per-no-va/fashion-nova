package com.supernova.fashionnova.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final List<CartItemDto> cartItemDtoList;

    private final int totalPrice;

    public CartResponseDto(List<CartItemDto> cartItemDtoList, int totalPrice) {
        this.cartItemDtoList = cartItemDtoList;
        this.totalPrice = totalPrice;
    }
}
