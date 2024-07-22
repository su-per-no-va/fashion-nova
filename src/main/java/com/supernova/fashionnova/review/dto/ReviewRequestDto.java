package com.supernova.fashionnova.review.dto;

import lombok.Getter;

@Getter
public class ReviewRequestDto {

    private Long productId;

    private String review;

    private int rating;

    private String reviewImageUrl;

}
