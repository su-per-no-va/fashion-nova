package com.supernova.fashionnova.review;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
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
}
