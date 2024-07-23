package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    private Long productDetailId;

    private int count;

    private String size;

    private String color;

}
