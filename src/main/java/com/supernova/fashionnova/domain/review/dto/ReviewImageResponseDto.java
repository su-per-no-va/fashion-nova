package com.supernova.fashionnova.domain.review.dto;

import com.supernova.fashionnova.domain.review.ReviewImage;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
public class ReviewImageResponseDto {

    private final Long id;
    private final String imageUrl;

    public ReviewImageResponseDto(ReviewImage reviewImage) {
        this.id = reviewImage.getId();
        this.imageUrl = reviewImage.getReviewImageUrl();
    }

}
