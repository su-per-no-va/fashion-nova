package com.supernova.fashionnova.domain.review.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ReviewDeleteRequestDto {

    private Long reviewId;

    public ReviewDeleteRequestDto(Long reviewId) {
        this.reviewId = reviewId;
    }

}
