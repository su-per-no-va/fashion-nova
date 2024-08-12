package com.supernova.fashionnova.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor // 기본 생성자 추가
public class CartRequestDto {

    private Long productId;

    private int count;

    private String size;

    private String color;

    public CartRequestDto(Long productId, int count, String size, String color) {
        this.productId = productId;
        this.count = count;
        this.size = size;
        this.color = color;
    }
}
