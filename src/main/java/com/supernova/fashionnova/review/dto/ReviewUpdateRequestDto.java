package com.supernova.fashionnova.review.dto;

import lombok.Getter;

@Getter
public class ReviewUpdateRequestDto {

    private Long reviewId;

    private String review;

    private int rating;

    private String reviewImageUrl;

}
