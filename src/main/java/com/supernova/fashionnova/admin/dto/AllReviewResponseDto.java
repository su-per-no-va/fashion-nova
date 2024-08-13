package com.supernova.fashionnova.admin.dto;

import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewImage;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Getter;

@Getter
public class AllReviewResponseDto {

    private final Long reviewId;
    private final Long userId;
    private final String name;
    private final Long productId;
    private final String productName;
    private final int rating;
    private final String content;
    private final List<String> imageUrls;
    private final LocalDateTime createdAt;

    public AllReviewResponseDto(Review review) {
        this.reviewId = review.getId();
        this.userId = review.getUser().getId();
        this.name = review.getUser().getName();
        this.productId = review.getProduct().getId();
        this.productName = review.getProduct().getProduct();
        this.rating = review.getRating();
        this.content = review.getReview();
        this.imageUrls = review.getReviewImageList()
            .stream()
            .map(ReviewImage::getReviewImageUrl)
            .toList();
        this.createdAt = review.getCreatedAt();

    }

}
