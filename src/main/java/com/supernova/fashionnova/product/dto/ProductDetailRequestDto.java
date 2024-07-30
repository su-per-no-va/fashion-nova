package com.supernova.fashionnova.product.dto;

import com.supernova.fashionnova.product.ProductStatus;
import lombok.Getter;

@Getter
public class ProductDetailRequestDto {

    private Long productDetailId;
    private String size;
    private String color;
    private Long quantity;
    private ProductStatus status;
}
