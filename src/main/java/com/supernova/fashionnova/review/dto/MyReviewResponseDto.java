package com.supernova.fashionnova.review.dto;

import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.ReviewImage;
import lombok.Getter;

@Getter
public class MyReviewResponseDto {

    private final Long id;
    private final String review;
    private final int rating;
    private final String product;
    private final String reviewImageUrl;

    public MyReviewResponseDto(Review review) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.product = review.getProduct().getProduct();
        this.reviewImageUrl = review.getReviewImageList().stream()
            .map(ReviewImage::getReviewImageUrl)
            .findFirst()
            .orElse(null); // ReviewImage를 List로 관리
    }
}
