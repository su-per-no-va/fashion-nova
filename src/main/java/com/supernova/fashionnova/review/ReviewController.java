package com.supernova.fashionnova.review;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.review.dto.MyReviewResponseDto;
import com.supernova.fashionnova.review.dto.ReviewDeleteRequestDto;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.review.dto.ReviewUpdateRequestDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

        return ResponseUtil.of(HttpStatus.CREATED, "리뷰 등록 완료");
    }

    /**
     * 상품별 리뷰 전체 조회
     *
     * @param productId 상품 ID
     * @return List<ReviewResponseDto>
     */
    @GetMapping("/{productId}")
    public ResponseEntity<Page<ReviewResponseDto>> getReviewsByProductId(
        @PathVariable Long productId,
        @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Review> reviews = reviewService.getReviewsByProductId(productId, pageable);
        Page<ReviewResponseDto> reviewResponseDtoPage = reviews.map(ReviewResponseDto::new);

        return ResponseUtil.of(HttpStatus.OK, reviewResponseDtoPage);
    }

    /**
     * 내가 작성한 리뷰 조회
     *
     * @param userDetails 로그인된 사용자 정보
     * @return List<MyReviewResponseDto>
     */
    @GetMapping
    public ResponseEntity<Page<MyReviewResponseDto>> getMyReviews(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestParam(defaultValue = "0") int page) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Review> reviews = reviewService.getReviewsByUser(userDetails.getUser(), pageable);
        Page<MyReviewResponseDto> myReviewResponseDtoPage = reviews.map(MyReviewResponseDto::new);

        return ResponseUtil.of(HttpStatus.OK, myReviewResponseDtoPage);
    }

    /**
     * 리뷰 수정
     *
     * @param userDetails 로그인된 사용자 정보
     * @param dto 리뷰 수정 요청 DTO
     * @return 리뷰 수정 완료
     */
    @PutMapping
    public ResponseEntity<String> updateReview(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto) {

        reviewService.updateReview(userDetails.getUser(), reviewUpdateRequestDto);

        return ResponseUtil.of(HttpStatus.OK, "리뷰 수정 완료");
    }

    /**
     * 리뷰 삭제
     *
     * @param userDetails 로그인된 사용자 정보
     * @param dto 리뷰 삭제 요청 DTO
     * @return 리뷰 삭제 완료
     */
    @DeleteMapping
    public ResponseEntity<String> deleteReview(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @RequestBody ReviewDeleteRequestDto reviewDeleteRequestDto) {

        reviewService.deleteReview(userDetails.getUser(), reviewDeleteRequestDto.getReviewId());

        return ResponseUtil.of(HttpStatus.OK, "리뷰 삭제 완료");
    }

}
