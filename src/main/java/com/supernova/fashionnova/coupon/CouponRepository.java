package com.supernova.fashionnova.coupon;

import com.supernova.fashionnova.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    List<Coupon> findByUserAndStatus(User user, CouponStatus couponStatus);
}
