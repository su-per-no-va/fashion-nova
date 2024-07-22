package com.supernova.fashionnova.cart.dto;

import lombok.Getter;

@Getter
public class CartDeleteRequestDto {

    private Long productDetailId;

    public Long getProductDetailId() {
        return productDetailId;
    }

    public void setProductDetailId(Long productDetailId) {
        this.productDetailId = productDetailId;
    }

}
