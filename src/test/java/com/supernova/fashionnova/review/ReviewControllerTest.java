package com.supernova.fashionnova.review;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.review.dto.ReviewDeleteRequestDto;
import com.supernova.fashionnova.review.dto.ReviewRequestDto;
import com.supernova.fashionnova.review.dto.ReviewUpdateRequestDto;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.User;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReviewService reviewService;

    private final String baseUrl = "/reviews";  // 테스트할 기본 URL을 설정합니다.
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @BeforeEach
    void setUp() {
        // 테스트 전에 매번 실행되는 설정 메서드입니다.

        // Given a mock UserDetailsImpl
        given(userDetails.getUsername()).willReturn("user");
        given(userDetails.getUser()).willReturn(new User(
            "testUSer",
            "Test1234!@",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678"
        ));

        // Set the security context
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("리뷰 등록 성공 테스트")
    void addReview() throws Exception {
        // given
        ReviewRequestDto requestDto = new ReviewRequestDto(1L, "좋아요", 5, "image.jpg");

        doNothing().when(reviewService).addReview(any(User.class), any(ReviewRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(post(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("리뷰 등록 완료"));
        verify(reviewService).addReview(any(User.class), any(ReviewRequestDto.class));
    }

    @Test
    @DisplayName("상품별 리뷰 전체 조회 테스트")
    void getReviewsByProductId() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviews = new PageImpl<>(Collections.emptyList(), pageable, 0);
        given(reviewService.getReviewsByProductId(anyLong(), any(Pageable.class))).willReturn(reviews);

        // when
        ResultActions result = mockMvc.perform(get(baseUrl + "/{productId}", 1L)
            .param("page", "0")
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isEmpty());
        verify(reviewService).getReviewsByProductId(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("내가 작성한 리뷰 조회 테스트")
    void getMyReviews() throws Exception {
        // given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Review> reviews = new PageImpl<>(Collections.emptyList(), pageable, 0);
        given(reviewService.getReviewsByUser(any(User.class), any(Pageable.class))).willReturn(reviews);

        // when
        ResultActions result = mockMvc.perform(get(baseUrl)
            .param("page", "0")
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isEmpty());
        verify(reviewService).getReviewsByUser(any(User.class), any(Pageable.class));
    }

    @Test
    @DisplayName("리뷰 수정 성공 테스트")
    void updateReviewSuccess() throws Exception {
        // given
        ReviewUpdateRequestDto requestDto = new ReviewUpdateRequestDto(1L, "수정된 리뷰", 4);

        doNothing().when(reviewService).updateReview(any(User.class), any(ReviewUpdateRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(put(baseUrl)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf())
            .principal(() -> userDetails.getUsername()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("리뷰 수정 완료"));
        verify(reviewService).updateReview(any(User.class), any(ReviewUpdateRequestDto.class));
    }

    @Test
    @DisplayName("리뷰 삭제 성공 테스트")
    void deleteReviewSuccess() throws Exception {
        // given
        ReviewDeleteRequestDto requestDto = new ReviewDeleteRequestDto(1L);

        doNothing().when(reviewService).deleteReview(any(User.class), anyLong());

        // when
        ResultActions result = mockMvc.perform(delete(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto))
                .with(csrf())
                .principal(() -> userDetails.getUsername()))
            .andDo(print()); // 요청 및 응답 내용을 출력하여 디버깅에 도움을 줍니다.

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("리뷰 삭제 완료"));
        verify(reviewService).deleteReview(any(User.class), anyLong());
    }
}