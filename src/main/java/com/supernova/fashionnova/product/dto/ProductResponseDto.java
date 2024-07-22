package com.supernova.fashionnova.product.dto;


import com.supernova.fashionnova.product.Product;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponseDto {
    private Long id;
    private String product;
    private Long price;
    private Long like_count;
    private Long review_count;
    private String product_status;
    private LocalDateTime created_at;


    public ProductResponseDto(Product product){
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.like_count = product.getLike_count();
        this.review_count = product.getReview_count();
        this.product_status = product.getProduct_status();
        this.created_at = product.getCreatedAt();
    }
}
