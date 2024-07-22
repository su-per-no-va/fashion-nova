package com.supernova.fashionnova.review;

import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * 리뷰 등록
     *
     * @param reviewRequestDto
     * @return "리뷰 등록 완료"
     */
    @PostMapping
    public ResponseEntity<String> addReview(
        @Valid @RequestBody ReviewRequestDto reviewRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        reviewService.addReview(userDetails.getUser(), reviewRequestDto);

        return new ResponseEntity<>("리뷰 등록 완료", HttpStatus.OK);
    }
}
