package com.supernova.fashionnova.product.dto;


import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductStatus;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final String product;
    private final int price;
    private final ProductStatus productStatus;
    private final int likeCount;
    private final int reviewCount;
    private final List<ProductDetailResponseDto> productDetails;
    private final LocalDateTime created_at;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.productStatus = product.getProductStatus();
        this.likeCount = product.getLikeCount();
        this.reviewCount = product.getReviewCount();
        this.productDetails = product.getProductDetailList().stream()
            .map(ProductDetailResponseDto::new)
            .toList();
        this.created_at = product.getCreatedAt();
    }

}
