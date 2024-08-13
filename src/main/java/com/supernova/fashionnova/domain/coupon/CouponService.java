package com.supernova.fashionnova.domain.coupon;

import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    /**
     * 보유 쿠폰 조회
     *
     * @param user
     * @return List<CouponResponseDto>
     */
    @Transactional
    public List<CouponResponseDto> getCouponList(User user) {

        updateCouponStatus(user);

        return getCouponListByStatus(user, CouponStatus.ACTIVE);
    }

    /**
     * 쿠폰 내역 조회
     *
     * @param user
     * @return List<CouponResponseDto>
     */
    @Transactional
    public List<CouponResponseDto> getUsedCouponList(User user) {

        updateCouponStatus(user);

        return getCouponListByStatus(user, CouponStatus.INACTIVE);
    }

    private void updateCouponStatus(User user) {

        List<Coupon> coupons = couponRepository.findByUser(user);

        coupons.forEach(Coupon::updateStatusIfExpired);

        couponRepository.saveAll(coupons);
    }

    private List<CouponResponseDto> getCouponListByStatus(User user, CouponStatus status) {

        List<Coupon> coupons = couponRepository.findByUserAndStatus(user, status);

        return coupons.stream()
            .map(CouponResponseDto::new)
            .collect(Collectors.toList());
    }

}
