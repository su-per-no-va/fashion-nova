package com.supernova.fashionnova.product.dto;


import com.supernova.fashionnova.product.Product;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final String product;
    private final int price;
    private final String product_status;
    private final int like_count;
    private final int review_count;
    private final List<ProductDetailResponseDto> productDetails;
    private final LocalDateTime created_at;

    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.product_status = product.getProductStatus().toString();
        this.like_count = product.getLikeCount();
        this.review_count = product.getReviewCount();
        this.productDetails = product.getProductDetails().stream()
            .map(ProductDetailResponseDto::new)
            .toList();
        this.created_at = product.getCreatedAt();
    }

}
