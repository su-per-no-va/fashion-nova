package com.supernova.fashionnova.user;

import com.supernova.fashionnova.address.Address;
import com.supernova.fashionnova.cart.Cart;
import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.wish.Wish;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userName;

    private String socialId;

    @Column(nullable = false)
    private String password;

    @Email
    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private UserStatus userStatus;

    @Column(nullable = false)
    private UserGrade userGrade;

    @Column(nullable = false)
    private Long mileage;

    // 주문
    @OneToMany(mappedBy = "user")
    private List<Order> orderList = new ArrayList<>();

    // 위시리스트
    @OneToMany(mappedBy = "user")
    private List<Wish> wishList = new ArrayList<>();

    // 쿠폰
    @OneToMany(mappedBy = "user")
    private List<Coupon> couponList = new ArrayList<>();

    // 배송지
    @OneToMany(mappedBy = "user")
    private List<Address> addressList = new ArrayList<>();

    // 경고
    @OneToMany(mappedBy = "user")
    private List<Warn> warnList = new ArrayList<>();

    // 문의
    @OneToMany(mappedBy = "user")
    private List<Question> questionList = new ArrayList<>();

    // 장바구니
    @OneToOne(mappedBy = "user")
    private Cart cart;

    // 리뷰
    @OneToMany(mappedBy = "user")
    private List<Review> reviewList = new ArrayList<>();

}
