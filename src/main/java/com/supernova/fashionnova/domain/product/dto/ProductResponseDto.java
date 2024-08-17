package com.supernova.fashionnova.domain.product.dto;

import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.review.ReviewImage;
import com.supernova.fashionnova.domain.review.dto.ReviewResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    private final List<ReviewResponseDto> reviews;
    private final LocalDateTime createdAt;
    private final String imageUrl;
    private final List<String> imageUrlList;
    private final String explanation;

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
        this.reviews = product.getReviews().stream()
            .map(review -> {
                List<String> imageUrls = review.getReviewImageList().stream()
                    .map(ReviewImage::getReviewImageUrl)
                    .collect(Collectors.toList());
                return new ReviewResponseDto(review, imageUrls);
            })
            .toList();
        this.createdAt = product.getCreatedAt();
        this.imageUrl = product.getImageUrl();
        this.imageUrlList = null;
        this.explanation = product.getExplanation();
    }

    public ProductResponseDto(Product product, List<String> imageUrlList) {
        this.id = product.getId();
        this.product = product.getProduct();
        this.price = product.getPrice();
        this.productStatus = product.getProductStatus();
        this.wishCount = product.getWishCount();
        this.reviewCount = product.getReviewCount();
        this.productDetails = product.getProductDetailList().stream()
            .map(ProductDetailResponseDto::new)
            .toList();
        this.reviews = product.getReviews().stream()
            .map(review -> {
                List<String> imageUrls = review.getReviewImageList().stream()
                    .map(ReviewImage::getReviewImageUrl)
                    .collect(Collectors.toList());
                return new ReviewResponseDto(review, imageUrls);
            })
            .toList();
        this.createdAt = product.getCreatedAt();
        this.imageUrl = product.getImageUrl();
        this.imageUrlList = imageUrlList;
        this.explanation = product.getExplanation();
    }

}
