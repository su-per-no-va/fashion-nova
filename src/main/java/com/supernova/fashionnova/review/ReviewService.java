package com.supernova.fashionnova.review;

import com.amazonaws.util.CollectionUtils;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.order.OrdersRepository;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductRepository;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.review.dto.ReviewUpdateRequestDto;
import com.supernova.fashionnova.upload.FileUploadUtil;
import com.supernova.fashionnova.upload.ImageType;
import com.supernova.fashionnova.user.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ProductRepository productRepository;

    private final OrdersRepository ordersRepository;

    private final FileUploadUtil fileUploadUtil;

    /**
     * 리뷰 등록
     *
     * @param reviewRequestDto
     * @param images
     * @throws CustomException NOT_ORDERED_PRODUCT 구매하지 않은 상품입니다.
     * @throws CustomException NOT_FOUND_PRODUCT 상품을 찾을 수 없습니다.
     */
    @Transactional
    public void addReview(User user, ReviewRequestDto reviewRequestDto, List<MultipartFile> images) {
        // 주문 내역 확인
        existsOrder(user.getId(), reviewRequestDto.getProductId());

        Product product = getProduct(reviewRequestDto.getProductId());

        Review review = new Review(user, product, reviewRequestDto.getReview(),
            reviewRequestDto.getRating());

        reviewRepository.save(review);


        product.increaseReview();

        //파일 업로드
        if (!CollectionUtils.isNullOrEmpty(images)) {
            fileUploadUtil.uploadImage(images, ImageType.REVIEW, review.getId());
        }

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
    public List<ReviewResponseDto> getReviewsByProductId(Long productId, Pageable pageable) {
        Product product = getProduct(productId);
        List<Review> reviews = reviewRepository.findByProduct(product, pageable);

        // 리뷰 ID 리스트 생성
        List<Long> reviewIds = reviews.stream().map(Review::getId).toList();

        // 리뷰 이미지 다운로드
        Map<Long, List<String>> reviewImages = fileUploadUtil.downloadImages(ImageType.REVIEW, reviewIds);

        // ReviewResponseDto 객체 생성 및 설정
        List<ReviewResponseDto> reviewResponseDtos = new ArrayList<>();
        for (Review review : reviews) {
            ReviewResponseDto dto = new ReviewResponseDto(review, reviewImages.get(review.getId())); // byte[] 리스트 설정
            reviewResponseDtos.add(dto);
        }

        return reviewResponseDtos;
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
     * @param user       사용자 정보
     * @param requestDto 리뷰 수정 요청 DTO
     * @throws CustomException NOT_FOUND_REVIEW 리뷰가 존재하지 않습니다.
     */
    @Transactional
    public void updateReview(User user, ReviewUpdateRequestDto requestDto) {
        Review review = getReview(user, requestDto.getReviewId());

        review.update(requestDto.getReview(), requestDto.getRating());

//        Optional<ReviewImage> existingImage = reviewImageRepository.findByReview(review);
//        if (requestDto.getReviewImageUrl() != null) {
//            if (existingImage.isPresent()) {
//                ReviewImage reviewImage = existingImage.get();
//                ReviewImage updatedReviewImage = new ReviewImage(reviewImage.getId(), reviewImage.getReview(), requestDto.getReviewImageUrl());
//                reviewImageRepository.save(updatedReviewImage);
//            } else {
//                ReviewImage reviewImage = new ReviewImage(null, review, requestDto.getReviewImageUrl());
//                review.addReviewImage(reviewImage);
//                reviewImageRepository.save(reviewImage);
//            }
//        } else {
//
//            existingImage.ifPresent(reviewImageRepository::delete);
//        }
    }

    /**
     * 리뷰 삭제
     *
     * @param user       사용자 정보
     * @param reviewId 리뷰 삭제 요청 DTO
     * @throws CustomException NOT_FOUND_REVIEW 리뷰가 존재하지 않습니다.
     */
    @Transactional
    public void deleteReview(User user, Long reviewId) {
        Review review = getReview(user, reviewId);
        reviewRepository.delete(review);
        review.getProduct().decreaseReview();
    }

    // 제품 조회 메서드
    private Product getProduct(Long productId) {
        return productRepository.findById(productId)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_PRODUCT));
    }

    // 리뷰 조회 메서드
    private Review getReview(User user, Long reviewId) {
        return reviewRepository.findByIdAndUser(reviewId, user)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_REVIEW));
    }

    // 사용자 주문 내역 확인 메서드
    private void existsOrder(Long userId, Long productId) {
        boolean hasOrdered = ordersRepository.existsByUserIdAndProductId(userId, productId);
        if (!hasOrdered) {
            throw new CustomException(ErrorType.NOT_ORDERED_PRODUCT);
        }
    }
}
