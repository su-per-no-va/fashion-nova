package com.supernova.fashionnova.domain.coupon;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.global.common.Timestamped;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "coupon")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date period;

    @Column(nullable = false)
    private String sale;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponType type;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CouponStatus status;

    @Builder
    public Coupon(User user, String name, Date period, String sale, CouponType type) {
        this.user = user;
        this.name = name;
        this.period = period;
        this.sale = sale;
        this.type = type;
        this.status = CouponStatus.ACTIVE;
    }

    public void useCoupon() {
        this.status = CouponStatus.INACTIVE;
    }

    public void updateStatusIfExpired() {
        Date currentDate = new Date();
        if (this.period.before(currentDate)) {
            this.status = CouponStatus.INACTIVE;
        }
    }

}
