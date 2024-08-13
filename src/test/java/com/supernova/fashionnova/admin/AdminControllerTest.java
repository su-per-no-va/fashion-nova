package com.supernova.fashionnova.admin;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.answer.dto.AnswerRequestDto;
import com.supernova.fashionnova.domain.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.domain.mileage.dto.MileageRequestDto;
import com.supernova.fashionnova.domain.product.Product;
import com.supernova.fashionnova.domain.product.ProductCategory;
import com.supernova.fashionnova.domain.product.ProductStatus;
import com.supernova.fashionnova.domain.question.dto.QuestionResponseDto;
import com.supernova.fashionnova.domain.review.Review;
import com.supernova.fashionnova.domain.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRole;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@WebMvcTest(AdminController.class)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AdminService adminService;

    private final String baseUrl = "/api/admin";

    private User user;

    private Product product;

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

        user.updateRole(UserRole.ADMIN);

        this.product = new Product(
            "꽃무늬 원피스",
            10000L,
            "겁나 멋진 원피스",
            ProductCategory.TOP,
            ProductStatus.ACTIVE
        );

        this.review = new Review(
            user,
            product,
            "너무 좋아요",
            5);
    }

    @Nested
    @DisplayName("작성자별 리뷰 조회 테스트")
    class getReviewsByUserId {

        @Test
        @DisplayName("작성자별 리뷰 조회 성공 테스트")
        @WithMockUser(roles = "ADMIN")
        void getReviewsByUserId1() throws Exception {
            // given
            List<ReviewResponseDto> reviews =
                Collections.singletonList(new ReviewResponseDto(review, null));

            when(adminService.getReviewListByUserId(anyLong(), anyInt())).thenReturn(reviews);

            // when
            ResultActions result = mockMvc.perform(get(baseUrl + "/reviews/{userId}", user.getId())
                    .param("page", "0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                .andDo(print());

            // then
            result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(reviews)));
        }

        @Test
        @DisplayName("작성자별 리뷰 조회 실패 테스트 - 유저 없음")
        @WithMockUser(roles = "ADMIN")
        void getReviewsByUserId2() throws Exception {
            // given
            when(adminService.getReviewListByUserId(anyLong(), anyInt())).thenThrow(
                new CustomException(ErrorType.NOT_FOUND_USER));

            // when
            ResultActions result = mockMvc.perform(get(baseUrl + "/reviews/{userId}", user.getId())
                    .param("page", "0")
                    .contentType(MediaType.APPLICATION_JSON)
                    .with(csrf()))
                .andDo(print());

            // then
            result.andExpect(status().isNotFound());
        }
    }

    @Test
    @DisplayName("답변 등록 테스트")
    @WithMockUser(roles = "ADMIN")
    void addAnswerTest() throws Exception {
        // given
        AnswerRequestDto requestDto = Mockito.mock(AnswerRequestDto.class);
        given(requestDto.getQuestionId()).willReturn(1L);
        given(requestDto.getAnswer()).willReturn("This is a test answer");

        doNothing().when(adminService).addAnswer(any(AnswerRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(post(baseUrl + "/answers")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf())
            .content(objectMapper.writeValueAsString(requestDto)));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("Q&A 답변 등록 완성"));
    }

    @Test
    @DisplayName("전체 문의 조회 테스트")
    @WithMockUser(roles = "ADMIN")
    void getQuestionListTest() throws Exception {
        // given
        List<QuestionResponseDto> responseDto = new ArrayList<>();

        given(adminService.getQuestionList(anyInt())).willReturn(responseDto);

        // when
        ResultActions result = mockMvc.perform(get(baseUrl + "/answers")
            .param("page", "0")
            .with(csrf()));

        // then
        result.andExpect(status().isOk());
        verify(adminService).getQuestionList(anyInt());
    }

    @Test
    @DisplayName("쿠폰 지급 테스트")
    @WithMockUser(roles = "ADMIN")
    void addCouponTest() throws Exception {
        // given
        CouponRequestDto requestDto = Mockito.mock(CouponRequestDto.class);
        given(requestDto.getUserId()).willReturn(1L);
        given(requestDto.getName()).willReturn("Test Coupon");
        given(requestDto.getSale()).willReturn("10");
        given(requestDto.getType()).willReturn("WELCOME");

        doNothing().when(adminService).addCoupon(any(CouponRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(post(baseUrl + "/coupons")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf()));

        // then
        result.andExpect(status().isCreated())
            .andExpect(content().string("쿠폰 지급 성공"));
        verify(adminService).addCoupon(any(CouponRequestDto.class));
    }

    @Test
    @DisplayName("마일리지 지급 테스트")
    @WithMockUser(roles = "ADMIN")
    void addMileageTest() throws Exception {
        // given
        MileageRequestDto requestDto = new MileageRequestDto(1L, 1000L);

        doNothing().when(adminService).addMileage(any(MileageRequestDto.class));

        // when
        ResultActions result = mockMvc.perform(post(baseUrl + "/mileages")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(requestDto))
            .with(csrf()));

        // then
        result.andExpect(status().isCreated())
            .andExpect(content().string("마일리지 지급 성공"));
        verify(adminService).addMileage(any(MileageRequestDto.class));
    }

    @Test
    @DisplayName("마일리지 초기화 테스트")
    @WithMockUser(roles = "ADMIN")
    void deleteMileageTest() throws Exception {
        // given
        doNothing().when(adminService).deleteMileage();

        // when
        ResultActions result = mockMvc.perform(delete(baseUrl + "/mileages")
            .contentType(MediaType.APPLICATION_JSON)
            .with(csrf()));

        // then
        result.andExpect(status().isOk())
            .andExpect(content().string("마일리지 초기화 성공"));
        verify(adminService).deleteMileage();
    }

}
