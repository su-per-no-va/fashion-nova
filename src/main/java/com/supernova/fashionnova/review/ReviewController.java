package com.supernova.fashionnova.review;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

        return ResponseUtil.of(HttpStatus.OK, "리뷰 등록 완료");
    }

    /**
     * 상품별 리뷰 전체 조회
     *
     * @param productId 상품 ID
     * @return 상품별 리뷰 리스트
     */
    @GetMapping("/{productId}")
    public ResponseEntity<List<ReviewResponseDto>> getReviewsByProductId(@PathVariable Long productId) {
        List<Review> reviews = reviewService.getReviewsByProductId(productId);
        List<ReviewResponseDto> reviewResponseDtoList = reviews.stream()
            .map(ReviewResponseDto::new)
            .toList();

        return ResponseUtil.of(HttpStatus.OK, reviewResponseDtoList);
    }
}
