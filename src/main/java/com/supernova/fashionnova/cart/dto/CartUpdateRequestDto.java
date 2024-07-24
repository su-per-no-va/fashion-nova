package com.supernova.fashionnova.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CartUpdateRequestDto {

    private Long productDetailId;

    private int count;

    private String size;

    private String color;

}
