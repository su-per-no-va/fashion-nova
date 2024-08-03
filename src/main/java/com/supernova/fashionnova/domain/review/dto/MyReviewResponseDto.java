package com.supernova.fashionnova.domain.review.dto;

import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewImage;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class MyReviewResponseDto {

    private final Long id;
    private final String review;
    private final int rating;
    private final String product;
    private final List<String> reviewImageUrl;

    public MyReviewResponseDto(Review review) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.product = review.getProduct().getProduct();
        this.reviewImageUrl = review.getReviewImageList().stream()
            .map(ReviewImage::getReviewImageUrl)
            .collect(Collectors.toList());
    }

}
