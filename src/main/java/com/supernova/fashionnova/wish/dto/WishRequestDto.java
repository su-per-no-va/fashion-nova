package com.supernova.fashionnova.wish.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WishRequestDto {

    private Long productId;

    public WishRequestDto(Long productId) {
        this.productId = productId;
    }

}
