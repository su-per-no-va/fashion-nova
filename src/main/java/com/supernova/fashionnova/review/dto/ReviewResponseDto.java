package com.supernova.fashionnova.review.dto;

import com.supernova.fashionnova.review.Review;
import java.util.List;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;

    private final String review;

    private final int rating;

    private final String username;

    private final List<String> imageUrls;

    public ReviewResponseDto(Review review, List<String> imageUrls) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.username = review.getUser().getUserName();
        this.imageUrls = imageUrls;
    }
}
