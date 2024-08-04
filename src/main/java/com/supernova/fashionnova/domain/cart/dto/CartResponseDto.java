package com.supernova.fashionnova.domain.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CartResponseDto {

    private final List<CartItemDto> cartItemDtoList;
    private final Long totalPrice;

    public CartResponseDto(List<CartItemDto> cartItemDtoList, Long totalPrice) {
        this.cartItemDtoList = cartItemDtoList;
        this.totalPrice = totalPrice;
    }

}
