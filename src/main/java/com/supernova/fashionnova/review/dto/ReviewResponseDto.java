package com.supernova.fashionnova.review.dto;

import com.supernova.fashionnova.review.Review;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;
    private final String review;
    private final int rating;
    private final String username;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.username = review.getUser().getUserName();
    }
}
