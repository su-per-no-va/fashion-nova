package com.supernova.fashionnova.domain.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private Long productId;
    private String review;
    private int rating;

    public ReviewRequestDto(Long productId, String review, int rating) {
        this.productId = productId;
        this.review = review;
        this.rating = rating;
    }

}
