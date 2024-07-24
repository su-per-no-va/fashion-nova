package com.supernova.fashionnova.review.dto;

import lombok.Getter;

@Getter
public class ReviewDeleteRequestDto {

    private Long reviewId;

    public ReviewDeleteRequestDto(Long reviewId) {
        this.reviewId = reviewId;
    }

}
