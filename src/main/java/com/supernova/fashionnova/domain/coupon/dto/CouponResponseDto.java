package com.supernova.fashionnova.domain.coupon.dto;

import com.supernova.fashionnova.domain.coupon.Coupon;
import com.supernova.fashionnova.domain.coupon.CouponType;
import java.util.Date;
import lombok.Getter;

@Getter
public class CouponResponseDto {

    private final String name;
    private final Date period;
    private final String sale;
    private final CouponType type;

    public CouponResponseDto(Coupon coupon) {
        this.name = coupon.getName();
        this.period = coupon.getPeriod();
        this.sale = coupon.getSale();
        this.type = coupon.getType();
    }

}