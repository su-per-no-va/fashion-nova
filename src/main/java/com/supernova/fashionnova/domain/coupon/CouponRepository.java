package com.supernova.fashionnova.domain.coupon;

import com.supernova.fashionnova.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

    List<Coupon> findByUserAndStatus(User user, CouponStatus couponStatus);

    List<Coupon> findByUser(User user);

}
