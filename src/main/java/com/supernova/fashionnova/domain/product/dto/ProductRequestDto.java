package com.supernova.fashionnova.domain.product.dto;

import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductStatus;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRequestDto {

    private Long productId;
    private String product;
    private Long price;
    private String explanation;
    private ProductCategory category;
    private ProductStatus productStatus;
    private List<ProductDetailRequestDto> productDetailList;

    public ProductRequestDto(String product, Long price, String explanation, ProductCategory category, ProductStatus productStatus, List<ProductDetailRequestDto> productDetailList) {
        this.product = product;
        this.price = price;
        this.explanation = explanation;
        this.category = category;
        this.productStatus = productStatus;
        this.productDetailList = productDetailList;
    }

}
