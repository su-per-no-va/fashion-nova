package com.supernova.fashionnova.review;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.review.dto.ReviewUpdateRequestDto;
import com.supernova.fashionnova.user.User;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrdersRepository ordersRepository;

    private final ReviewImageRepository reviewImageRepository;

    /**
     * 리뷰 등록
     *
     * @param reviewRequestDto
     * @throws CustomException NOT_ORDERED_PRODUCT 구매하지 않은 상품입니다.
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾을 수 없습니다.
     */
    @Transactional
    public void addReview(User user, ReviewRequestDto reviewRequestDto) {
        // 주문 내역 확인
        boolean hasOrdered = ordersRepository.existsByUserIdAndProductId(user.getId(), reviewRequestDto.getProductId());
        if (!hasOrdered) {
            throw new CustomException(ErrorType.NOT_ORDERED_PRODUCT);
        }

        Product product = productRepository.findById(reviewRequestDto.getProductId())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        Review review = new Review(user, product, reviewRequestDto.getReview(), reviewRequestDto.getRating());
        reviewRepository.save(review);
    }

    /**
     * 상품별 리뷰 전체 조회
     *
     * @param productId 상품 ID
     * @return 상품별 리뷰 리스트
     * @return List<ReviewResponseDto>
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾을 수 없습니다.
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByProductId(Long productId, Pageable pageable) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        return reviewRepository.findByProduct(product, pageable);
    }

    /**
     * 내가 작성한 리뷰 조회
     *
     * @param user 사용자 정보
     * @return List<MyReviewResponseDto>
     */
    @Transactional(readOnly = true)
    public Page<Review> getReviewsByUser(User user, Pageable pageable) {
        return reviewRepository.findByUser(user, pageable);
    }

    /**
     * 리뷰 수정
     *
     * @param user 사용자 정보
     * @param requestDto 리뷰 수정 요청 DTO
     * @throws CustomException NOT_FOUND_REVIEW 리뷰가 존재하지 않습니다.
     */
    @Transactional
    public void updateReview(User user, ReviewUpdateRequestDto requestDto) {
        Review review = reviewRepository.findByIdAndUser(requestDto.getReviewId(), user)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REVIEW));

        review.update(requestDto.getReview(), requestDto.getRating());

        Optional<ReviewImage> existingImage = reviewImageRepository.findByReview(review);
        if (requestDto.getReviewImageUrl() != null) {
            if (existingImage.isPresent()) {
                ReviewImage reviewImage = existingImage.get();
                ReviewImage updatedReviewImage = new ReviewImage(reviewImage.getId(), reviewImage.getReview(), requestDto.getReviewImageUrl());
                reviewImageRepository.save(updatedReviewImage);
            } else {
                ReviewImage reviewImage = new ReviewImage(null, review, requestDto.getReviewImageUrl());
                review.addReviewImage(reviewImage);
                reviewImageRepository.save(reviewImage);
            }
        } else {

            existingImage.ifPresent(reviewImageRepository::delete);
        }
    }

    /**
     * 리뷰 삭제
     *
     * @param user 사용자 정보
     * @param requestDto 리뷰 삭제 요청 DTO
     * @throws CustomException NOT_FOUND_REVIEW 리뷰가 존재하지 않습니다.
     */
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = reviewRepository.findByIdAndUser(reviewId, user)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REVIEW));
        reviewRepository.delete(review);
    }

}
