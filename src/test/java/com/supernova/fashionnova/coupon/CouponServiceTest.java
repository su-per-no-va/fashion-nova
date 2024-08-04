package com.supernova.fashionnova.coupon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.BDDMockito.given;

import com.supernova.fashionnova.domain.coupon.Coupon;
import com.supernova.fashionnova.domain.coupon.CouponRepository;
import com.supernova.fashionnova.domain.coupon.CouponService;
import com.supernova.fashionnova.domain.coupon.CouponStatus;
import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

    @Mock
    private CouponRepository couponRepository;

    @InjectMocks
    private CouponService couponService;

    @Test
    @DisplayName("보유 쿠폰 조회 테스트")
    void getCouponListTest() {

        // given
        User user = Mockito.mock(User.class);
        Coupon coupon = Mockito.mock(Coupon.class);

        given(coupon.getName()).willReturn("웰컴쿠폰");

        given(couponRepository.findByUserAndStatus(user, CouponStatus.ACTIVE))
            .willReturn(List.of(coupon));

        // when
        List<CouponResponseDto> result = couponService.getCouponList(user);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        CouponResponseDto couponResponseDto = result.get(0);
        assertEquals("웰컴쿠폰", couponResponseDto.getName());
    }

    @Test
    @DisplayName("쿠폰 내역 조회 테스트")
    void getUsedCouponListTest() {

        // given
        User user = Mockito.mock(User.class);
        Coupon coupon = Mockito.mock(Coupon.class);

        given(coupon.getName()).willReturn("웰컴쿠폰");

        given(couponRepository.findByUserAndStatus(user, CouponStatus.INACTIVE))
            .willReturn(List.of(coupon));

        // when
        List<CouponResponseDto> result = couponService.getUsedCouponList(user);

        // then
        assertNotNull(result);
        assertEquals(1, result.size());

        CouponResponseDto couponResponseDto = result.get(0);
        assertEquals("웰컴쿠폰", couponResponseDto.getName());
    }

}
