package com.supernova.fashionnova.coupon.dto;

import jakarta.validation.constraints.Pattern;
import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponRequestDto {

    private Long userId;
    private String name;
    private Date period;
    private String sale;

    @Pattern(regexp = "WELCOME|GRADE_UP|REGULAR",
        message = "유효하지 않은 쿠폰 타입입니다.")
    private String type;

}
