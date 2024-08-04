package com.supernova.fashionnova.domain.cart.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CartDeleteRequestDto {

    private Long productDetailId;

    public CartDeleteRequestDto(Long productDetailId) {
        this.productDetailId = productDetailId;
    }

}
