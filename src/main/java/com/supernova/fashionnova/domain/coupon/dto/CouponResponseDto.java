package com.supernova.fashionnova.domain.coupon.dto;

import com.supernova.fashionnova.domain.coupon.Coupon;
import com.supernova.fashionnova.domain.coupon.CouponType;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.Getter;

@Getter
public class CouponResponseDto {

    private final Long id;
    private final String name;
    private final LocalDateTime createdAt;
    private final Date period;
    private final String sale;
    private final CouponType type;

    public CouponResponseDto(Coupon coupon) {
        this.id = coupon.getId();
        this.name = coupon.getName();
        this.period = coupon.getPeriod();
        this.sale = coupon.getSale();
        this.type = coupon.getType();
        this.createdAt = coupon.getCreatedAt();
    }

}
