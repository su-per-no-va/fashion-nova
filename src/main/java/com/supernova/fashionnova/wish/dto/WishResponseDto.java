package com.supernova.fashionnova.wish.dto;

import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.wish.Wish;
import lombok.Getter;

@Getter
public class WishResponseDto {

    private final Product product;
    private final boolean isWish;

    public WishResponseDto(Wish wish) {
        this.product = wish.getProduct();
        this.isWish = wish.isWish();
    }

}
