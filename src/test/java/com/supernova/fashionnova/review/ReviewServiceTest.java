package com.supernova.fashionnova.review;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.supernova.fashionnova.domain.order.OrdersRepository;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductRepository;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.ReviewImageRepository;
import com.supernova.fashionnova.domain.review.ReviewRepository;
import com.supernova.fashionnova.domain.review.ReviewService;
import com.supernova.fashionnova.domain.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.domain.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.domain.review.dto.ReviewUpdateRequestDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.global.upload.FileUploadUtil;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OrdersRepository ordersRepository;

    @Mock
    private FileUploadUtil fileUploadUtil;

    @Mock
    private ReviewImageRepository reviewImageRepository;

    @InjectMocks
    private ReviewService reviewService;

    private User user;

    private Product product;

    private ReviewRequestDto reviewRequestDto;

    private ReviewUpdateRequestDto reviewUpdateRequestDto;

    private Review review;

    @BeforeEach
    void setUp() {

        this.user = User.builder()
            .userName("testUser1234")
            .name("테스트유저")
            .password("test1234!#")
            .email("test@gmail.com")
            .phone("010-1234-5678")
            .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        this.product = new Product(
            "꽃무늬 원피스",
            10000L,
            "겁나 멋진 원피스",
            ProductCategory.TOP,
            ProductStatus.ACTIVE
        );

        this.reviewRequestDto = new ReviewRequestDto(
            1L,
            "너무 좋아요",
            5);

        this.review = new Review(
            user,
            product,
            "너무 좋아요",
            5);

        this.reviewUpdateRequestDto = new ReviewUpdateRequestDto(
            1L,
            "너무 별로에요",
            3
        );
    }

    @Nested
    @DisplayName("리뷰 등록 테스트")
    class AddReviewTest {

        @Test
        @DisplayName("리뷰 등록 성공 테스트")
        void AddReviewTest1() {
            // given
            given(ordersRepository.existsByUserIdAndProductId(anyLong(), anyLong()))
                .willReturn(true);
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            when(reviewRepository.save(any(Review.class))).thenReturn(review);

            // when
            reviewService.addReview(user, reviewRequestDto, null);

            // then
            verify(ordersRepository).existsByUserIdAndProductId(anyLong(), anyLong());
            verify(productRepository).findById(anyLong());
            verify(reviewRepository).save(any(Review.class));
        }

        @Test
        @DisplayName("리뷰 등록 실패 테스트 - 주문 내역 없음")
        void AddReviewTest2() {
            // given
            given(ordersRepository.existsByUserIdAndProductId(anyLong(), anyLong()))
                .willReturn(false);

            // when / then
            assertThrows(CustomException.class,
                () -> reviewService.addReview(user, reviewRequestDto, null));
        }

        @Test
        @DisplayName("리뷰 등록 실패 테스트 - 상품 정보 없음")
        void AddReviewTest3() {
            // given
            given(ordersRepository.existsByUserIdAndProductId(anyLong(), anyLong()))
                .willReturn(true);
            given(productRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            CustomException exception = assertThrows(
                CustomException.class, () -> reviewService.addReview(user, reviewRequestDto, null));
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND_PRODUCT);
        }
    }

    @Nested
    @DisplayName("리뷰 조회 테스트")
    class GetReviewsByUserTest {

        @Test
        @DisplayName("제품별 리뷰 조회 테스트")
        void GetReviewsByUserTest1() {
            // given
            Pageable pageable = PageRequest.of(0, 10);
            List<Review> reviewPage = List.of(review);

            lenient().when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            lenient().when(reviewRepository.findByProduct(any(Product.class), any(Pageable.class)))
                .thenReturn(reviewPage);

            // when
            List<ReviewResponseDto> result = reviewService.getReviewsByProductId(1L, pageable);

            // then
            assertThat(result.get(0).getReview()).isNotEmpty();
            assertThat(result.get(0).getReview()).isEqualTo("너무 좋아요");
        }

        @Test
        @DisplayName("내가 작성한 리뷰 조회 테스트")
        void getReviewsByProductIdTest() {
            // given
            Pageable pageable = mock(Pageable.class);
            Page<Review> reviewPage = new PageImpl<>(List.of(mock(Review.class)));

            when(reviewRepository.findByUser(any(User.class), any(Pageable.class)))
                .thenReturn(reviewPage);

            // when
            Page<Review> result = reviewService.getReviewsByUser(user, pageable);

            // then
            assertThat(result).isEqualTo(reviewPage);
        }
    }

    @Nested
    @DisplayName("리뷰 수정 테스트")
    class UpdateReviewTest {

        @Test
        @DisplayName("리뷰 수정 성공 테스트")
        void UpdateReviewTest1() {
            // given
            lenient().when(reviewRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.of(review));

            // when
            reviewService.updateReview(user, reviewUpdateRequestDto);

            // then
            verify(reviewRepository, times(0)).save(review);
        }

        @Test
        @DisplayName("리뷰 수정 실패 테스트 - 리뷰 없음")
        void UpdateReviewTest2() {
            // given
            lenient().when(reviewRepository.findByIdAndUser(anyLong(), any(User.class)))
                .thenReturn(Optional.empty());

            // when / then
            assertThrows(CustomException.class,
                () -> reviewService.updateReview(user, reviewUpdateRequestDto));
        }
    }


    @Nested
    @DisplayName("리뷰 삭제 테스트")
    class DeleteReviewTest {

        @Test
        @DisplayName("리뷰 삭제 성공 테스트")
        void DeleteReviewTest1() {
            // given
            given(reviewRepository.findByIdAndUser(anyLong(), any(User.class))).willReturn(
                Optional.of(review));
            doNothing().when(reviewRepository).delete(any(Review.class));

            // when
            assertDoesNotThrow(() -> reviewService.deleteReview(user, 1L));

            // then
            verify(reviewRepository).delete(any(Review.class));
        }


        @Test
        @DisplayName("리뷰 삭제 실패 테스트 - 리뷰 없음")
        void DeleteReviewTest2() {
            // given
            given(reviewRepository.findByIdAndUser(anyLong(), any(User.class))).willReturn(
                Optional.empty());

            // when / then
            assertThrows(CustomException.class, () -> reviewService.deleteReview(user, 1L));
        }

    }

}
