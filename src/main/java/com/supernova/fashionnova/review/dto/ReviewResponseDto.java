package com.supernova.fashionnova.review.dto;

import com.supernova.fashionnova.review.Review;
import java.util.List;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ReviewResponseDto {

    private final Long id;

    private final String review;

    private final int rating;

    private final String username;

    private final List<String> imageUrls;

    public ReviewResponseDto(Review review, List<String> imageBytes) {
        this.id = review.getId();
        this.review = review.getReview();
        this.rating = review.getRating();
        this.username = review.getUser().getUserName();
        this.imageUrls = imageBytes;
    }
}
