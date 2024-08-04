package com.supernova.fashionnova.domain.product.dto;

import com.supernova.fashionnova.domain.product.ProductStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailRequestDto {

    private Long productDetailId;
    private String size;
    private String color;
    private Long quantity;
    private ProductStatus status;

    public ProductDetailRequestDto(String size, String color, Long quantity, ProductStatus status) {
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.status = status;
    }

}
