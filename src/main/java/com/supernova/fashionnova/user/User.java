package com.supernova.fashionnova.user;

import com.supernova.fashionnova.address.Address;
import com.supernova.fashionnova.cart.Cart;
import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.order.Order;
import com.supernova.fashionnova.question.Question;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.wish.Wish;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import lombok.Builder;
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

    @Column(nullable = false, unique = true)
    private String userName;

    private String socialId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserStatus userStatus;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserGrade userGrade;

    @Column(nullable = false)
    private Long mileage;

    private String refreshToken;

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

    @Builder
    public User(String userName, String password, String name, String email, String phone) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userRole = UserRole.ROLE_USER; // 기본적으로 USER로 권한 설정
        this.userStatus = UserStatus.MEMBER; // 처음 생성될때는 활성화 상태
        this.userGrade = UserGrade.BRONZE; // 처음 생성될 때는 브론즈
        this.mileage = 0L; // 처음 생성될 때는 0
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateStatus(UserStatus status) {
        this.userStatus = status;
    }

}
