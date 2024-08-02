package com.supernova.fashionnova.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private Long productId;

    private String review;

    private int rating;

    private String reviewImageUrl;

    public ReviewRequestDto(Long productId, String review, int rating, String reviewImageUrl) {
        this.productId = productId;
        this.review = review;
        this.rating = rating;
        this.reviewImageUrl = reviewImageUrl;
    }

}
