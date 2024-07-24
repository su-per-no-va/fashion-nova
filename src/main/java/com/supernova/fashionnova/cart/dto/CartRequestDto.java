package com.supernova.fashionnova.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartRequestDto {

    private Long productId;

    private int count;

    private String size;

    private String color;

}
