package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartRequestDto {

    private Long productId;

    private String size;

    private String color;

    private int count;
}
