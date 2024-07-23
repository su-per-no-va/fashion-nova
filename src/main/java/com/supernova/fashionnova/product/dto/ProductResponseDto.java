package com.supernova.fashionnova.product.dto;


import com.supernova.fashionnova.product.Product;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ProductResponseDto {

    private final Long id;
    private final int price;
    private final int like_count;
    private final String product;
    private final int review_count;
    private final String product_status;
    private final LocalDateTime created_at;


    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.like_count = product.getLike_count();
        this.review_count = product.getReview_count();
        this.product_status = product.getProduct_status();
        this.created_at = product.getCreatedAt();
    }
}
