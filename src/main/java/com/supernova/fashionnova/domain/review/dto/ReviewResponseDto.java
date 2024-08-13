package com.supernova.fashionnova.domain.review.dto;

import com.supernova.fashionnova.domain.review.Review;
import java.util.List;
import lombok.Getter;

@Getter
public class ReviewResponseDto {

    private final Long id;
    private final String review;
    private final int rating;
    private final String username;
    private final List<String> imageUrls;
    private final List<ReviewImageResponseDto> reviewImages;

    public ReviewResponseDto(Review review, List<String> imageUrls) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.username = review.getUser().getUserName();
        this.imageUrls = imageUrls;
        this.reviewImages = review.getReviewImageList().stream()
            .map(ReviewImageResponseDto::new)
            .toList();
    }

}
