package com.supernova.fashionnova.domain.coupon;

import com.supernova.fashionnova.domain.coupon.dto.CouponResponseDto;
import com.supernova.fashionnova.domain.order.Order;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserGrade;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.payment.PayAction;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
    public List<CouponResponseDto> getCouponList(User user) {

        return getCouponListByStatus(user, CouponStatus.ACTIVE);
    }

    /**
     * 쿠폰 내역 조회
     *
     * @param user
     * @return List<CouponResponseDto>
     */
    public List<CouponResponseDto> getUsedCouponList(User user) {

        return getCouponListByStatus(user, CouponStatus.INACTIVE);
    }

    private List<CouponResponseDto> getCouponListByStatus(User user, CouponStatus status) {

        List<Coupon> coupons = couponRepository.findByUserAndStatus(user, status);

        return coupons.stream()
            .map(CouponResponseDto::new)
            .collect(Collectors.toList());
    }
@Transactional
  public void calculateCoupon(PayAction action, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
          ()-> new CustomException(ErrorType.NOT_FOUND_COUPON));

        if(PayAction.BUY.equals(action)){
            coupon.useCoupon();
        }
        else{
          coupon.useCoupon();
        }
  }
}
