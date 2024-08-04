package com.supernova.fashionnova.domain.product.dto;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductDetailCreateDto {

    private Long productId;
    private List<ProductDetailRequestDto> productDetailRequestDtoList;

    public ProductDetailCreateDto(Long productId, List<ProductDetailRequestDto> productDetailRequestDtoList) {
        this.productId = productId;
        this.productDetailRequestDtoList = productDetailRequestDtoList;
    }

}
