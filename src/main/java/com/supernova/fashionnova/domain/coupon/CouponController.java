package com.supernova.fashionnova.domain.coupon;

import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import com.supernova.fashionnova.global.util.ResponseUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/coupons")
public class CouponController {

    private final CouponService couponService;

    /**
     * 보유 쿠폰 조회
     *
     * @param userDetails
     * @return List<CouponResponseDto>
     */
    @GetMapping
    public ResponseEntity<List<CouponResponseDto>> getCouponList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<CouponResponseDto> responseDto = couponService.getCouponList(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 쿠폰 내역 조회
     *
     * @param userDetails
     * @return List<CouponResponseDto>
     */
    @GetMapping("/used")
    public ResponseEntity<List<CouponResponseDto>> getUsedCouponList(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<CouponResponseDto> responseDto = couponService.getUsedCouponList(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

}
