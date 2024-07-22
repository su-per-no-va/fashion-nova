package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartUpdateRequestDto {

    private Long productId;

    private Integer count;

    private String size;

    private String color;

}
