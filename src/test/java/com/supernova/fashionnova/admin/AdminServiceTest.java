package com.supernova.fashionnova.admin;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.ReviewRepository;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import java.util.Collections;
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
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private AdminService adminService;

    private User user;
    private Review review;

    @BeforeEach
    void setUp() {
        user = new User(
            "testUser",
            "test1234",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678");

        review = new Review(
            user,
            1,
            "리뷰내용",
            5);
    }
    @Nested
    @DisplayName("리뷰 등록 테스트")
    class getReviewsByUserId {

        @Test
        @DisplayName("작성자별 리뷰 조회 성공 테스트")
        void getReviewsByUserId1() {
            // given
            given(userRepository.findById(anyLong())).willReturn(Optional.of(user));
            Page<Review> reviewPage = new PageImpl<>(Collections.singletonList(review));
            given(reviewRepository.findByUser(any(User.class), any(Pageable.class))).willReturn(
                reviewPage);

            // when
            List<ReviewResponseDto> reviews = adminService.getReviewsByUserId(1L, 0);

            // then
            assertThat(reviews).isNotEmpty();
            assertThat(reviews.get(0).getReview()).isEqualTo(review.getReview());
        }

        @Test
        @DisplayName("작성자별 리뷰 조회 실패 테스트 - 유저 없음")
        void getReviewsByUserId2() {
            // given
            given(userRepository.findById(anyLong())).willReturn(Optional.empty());

            // when / then
            CustomException exception = assertThrows(CustomException.class,
                () -> adminService.getReviewsByUserId(1L, 0));
            assertThat(exception.getErrorType()).isEqualTo(ErrorType.NOT_FOUND_USER);
        }
    }
}