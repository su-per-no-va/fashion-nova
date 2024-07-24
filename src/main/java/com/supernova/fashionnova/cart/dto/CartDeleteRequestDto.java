package com.supernova.fashionnova.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CartDeleteRequestDto {

    private Long productDetailId;

}
