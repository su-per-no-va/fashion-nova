package com.supernova.fashionnova.review;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.review.dto.ReviewDeleteRequestDto;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrdersRepository ordersRepository;

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
     * 상품별 리뷰 조회
     *
     * @param productId 상품 ID
     * @return 상품별 리뷰 리스트
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾을 수 없습니다.
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByProductId(Long productId) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));

        return reviewRepository.findByProduct(product);
    }

    /**
     * 사용자별 리뷰 조회
     *
     * @param user 사용자 정보
     * @return 사용자별 리뷰 리스트
     */
    @Transactional(readOnly = true)
    public List<Review> getReviewsByUser(User user) {
        return reviewRepository.findByUser(user);
    }

    /**
     * 리뷰 수정
     *
     * @param user 사용자 정보
     * @param requestDto 리뷰 수정 요청 DTO
     * 익셉션주석 추가
     */
//    @Transactional
//    public void updateReview(User user, ReviewUpdateRequestDto requestDto) {
//        Review review = reviewRepository.findByIdAndUser(requestDto.getReviewId(), user)
//            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REVIEW));
//
//        review.update(requestDto.getReview(), requestDto.getRating());
//
//        if (requestDto.getReviewImageUrl() != null) {
//            Optional<ReviewImage> existingImage = reviewImageRepository.findByReview(review);
//            if (existingImage.isPresent()) {
//                ReviewImage reviewImage = existingImage.get();
//                reviewImage.setReviewImageUrl(requestDto.getReviewImageUrl());
//            } else {
//                ReviewImage reviewImage = new ReviewImage();
//                reviewImage.setReview(review);
//                reviewImage.setReviewImageUrl(requestDto.getReviewImageUrl());
//                review.addReviewImage(reviewImage);
//                reviewImageRepository.save(reviewImage);
//            }
//        }
//    }

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
