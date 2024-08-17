package com.supernova.fashionnova.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {

    private Long reviewId;
    private String review;
    private int rating;

    public ReviewUpdateRequestDto(Long reviewId, String review, int rating) {
        this.reviewId = reviewId;
        this.review = review;
        this.rating = rating;
    }

}
