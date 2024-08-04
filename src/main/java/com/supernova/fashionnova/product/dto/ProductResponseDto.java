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
    private final Long price;
    private final ProductStatus productStatus;
    private final int wishCount;
    private final int reviewCount;
    private final List<ProductDetailResponseDto> productDetails;
    private final LocalDateTime createdAt;
    private final String imageUrl;
// 나 한승훈인데 여기 나보다 바보인사람 없다
    //나 김나은인데 나 바보맞다
    public ProductResponseDto(Product product) {
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.productStatus = product.getProductStatus();
        this.wishCount = product.getWishCount();
        this.reviewCount = product.getReviewCount();
        this.productDetails = product.getProductDetailList().stream()
            .map(ProductDetailResponseDto::new)
            .toList();
        this.createdAt = product.getCreatedAt();
        this.imageUrl = product.getImageUrl();
    }

}
