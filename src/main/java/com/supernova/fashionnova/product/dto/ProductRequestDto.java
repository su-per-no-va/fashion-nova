package com.supernova.fashionnova.product.dto;


import com.supernova.fashionnova.product.ProductCategory;
import com.supernova.fashionnova.product.ProductStatus;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductRequestDto {

    private String product;
    private int price;
    private String explanation;
    private ProductCategory category;
    private ProductStatus productStatus;
    private List<ProductDetailRequestDto> productDetailList;
}
