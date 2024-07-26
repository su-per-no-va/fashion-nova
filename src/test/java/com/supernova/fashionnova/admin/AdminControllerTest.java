package com.supernova.fashionnova.admin;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.product.Product;
import com.supernova.fashionnova.product.ProductCategory;
import com.supernova.fashionnova.product.ProductStatus;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRole;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
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

    private final String baseUrl = "/admin";

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
            10000,
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
        void getReviewsByUserId1 () throws Exception {
        // given
        List<ReviewResponseDto> reviews = Collections.singletonList(new ReviewResponseDto(review));

        when(adminService.getReviewsByUserId(anyLong(), anyInt())).thenReturn(reviews);

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
        void getReviewsByUserId2 () throws Exception {
        // given
        when(adminService.getReviewsByUserId(anyLong(), anyInt())).thenThrow(
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
}