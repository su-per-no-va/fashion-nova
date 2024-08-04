package com.supernova.fashionnova.domain.wish.dto;

import com.supernova.fashionnova.domain.product.dto.ProductResponseDto;
import com.supernova.fashionnova.domain.wish.Wish;
import lombok.Getter;

@Getter
public class WishResponseDto {

    private final Long id;
    private final ProductResponseDto product;

    public WishResponseDto(Wish wish) {
        this.id = wish.getId();
        this.product = new ProductResponseDto(wish.getProduct());
    }

}
