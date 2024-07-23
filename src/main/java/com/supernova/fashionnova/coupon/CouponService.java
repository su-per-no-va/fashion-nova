package com.supernova.fashionnova.coupon;

import com.supernova.fashionnova.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    public List<CouponResponseDto> getCouponList(User user) {

        List<Coupon> coupons = couponRepository.findByUserAndIsCouponUsed(user, false);

        return coupons.stream()
            .map(CouponResponseDto::new)
            .collect(Collectors.toList());
    }

    public List<CouponResponseDto> getUsedCouponList(User user) {

        List<Coupon> coupons = couponRepository.findByUserAndIsCouponUsed(user, true);

        return coupons.stream()
            .map(CouponResponseDto::new)
            .collect(Collectors.toList());
    }

}
