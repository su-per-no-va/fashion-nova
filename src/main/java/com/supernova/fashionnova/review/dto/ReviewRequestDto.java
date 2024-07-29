package com.supernova.fashionnova.review.dto;

import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
public class ReviewRequestDto {

    private Long productId;

    private String review;

    private int rating;

    private String reviewImageUrl;

}
