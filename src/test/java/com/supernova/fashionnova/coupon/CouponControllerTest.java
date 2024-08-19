/*
package com.supernova.fashionnova.coupon;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.coupon.Coupon;
import com.supernova.fashionnova.domain.coupon.CouponController;
import com.supernova.fashionnova.domain.coupon.CouponService;
import com.supernova.fashionnova.domain.coupon.CouponType;
import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CouponController.class)
class CouponControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final String baseUrl = "/coupons";
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @MockBean
    private CouponService couponService;

    @BeforeEach
    void setUp() {

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
    @DisplayName("보유 쿠폰 조회 테스트")
    void getCouponListTest() throws Exception {

        // given
        User user = userDetails.getUser();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2024-08-02");
        Coupon coupon = new Coupon(user, "신규 회원 쿠폰", date, "20%", CouponType.WELCOME);
        List<CouponResponseDto> responseDtoList = List.of(new CouponResponseDto(coupon));

        // when
        when(couponService.getCouponList(user)).thenReturn(responseDtoList);

        // then
        mockMvc.perform(get(baseUrl)
                .with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(responseDtoList.size()),
                jsonPath("$[0].name").value("신규 회원 쿠폰"),
                jsonPath("$[0].sale").value("20%"),
                jsonPath("$[0].type").value("WELCOME")
            );

    }

    @Test
    @DisplayName("쿠폰 내역 조회 테스트")
    void getUsedCouponListTest() throws Exception {

        // given
        User user = userDetails.getUser();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = format.parse("2024-08-02");
        Coupon coupon = new Coupon(user, "등급 상승 쿠폰", date, "15%", CouponType.GRADE_UP);
        coupon.useCoupon();
        List<CouponResponseDto> responseDtoList = List.of(new CouponResponseDto(coupon));

        // when
        when(couponService.getUsedCouponList(user)).thenReturn(responseDtoList);

        // then
        mockMvc.perform(get(baseUrl + "/used")
                .with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(responseDtoList.size()),
                jsonPath("$[0].name").value("등급 상승 쿠폰"),
                jsonPath("$[0].sale").value("15%"),
                jsonPath("$[0].type").value("GRADE_UP")
            );

    }

}

 */