package com.supernova.fashionnova.coupon.dto;

import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.coupon.CouponType;
import com.supernova.fashionnova.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.Builder;
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
