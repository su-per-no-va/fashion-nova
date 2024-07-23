package com.supernova.fashionnova.product.dto;

import com.supernova.fashionnova.product.ProductDetail;
import lombok.Getter;

@Getter
public class ProductDetailResponseDto {

    private final String color;
    private final String size;

    public ProductDetailResponseDto (ProductDetail productDetail) {
        this.color = productDetail.getColor();
        this.size = productDetail.getSize();

    }

}
