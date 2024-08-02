package com.supernova.fashionnova.user;

import com.supernova.fashionnova.address.Address;
import com.supernova.fashionnova.coupon.Coupon;
import com.supernova.fashionnova.global.common.Timestamped;
import com.supernova.fashionnova.mileage.Mileage;
import com.supernova.fashionnova.user.dto.UserUpdateRequestDto;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String userName;

    private Long kakaoId;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column
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
    private Long mileage = 0L;

    private String refreshToken;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mileage> mileageList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> couponList = new ArrayList<>();

    //결제 고유번호
    private String tid;

    @Builder
    public User(String userName, String password, String name, String email, String phone) {
        this.userName = userName;
        this.password = password;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.userRole = UserRole.USER; // 기본적으로 USER로 권한 설정
        this.userStatus = UserStatus.MEMBER; // 처음 생성될때는 활성화 상태
        this.userGrade = UserGrade.BRONZE; // 처음 생성될 때는 브론즈
        this.mileage = 0L; // 처음 생성될 때는 0
    }

    public void updateUser(UserUpdateRequestDto requestDto,String encodedPassword) {
        this.userName = requestDto.getUserName();
        this.password = encodedPassword;
        this.name = requestDto.getName();
        this.email = requestDto.getEmail();
        this.phone = requestDto.getPhone();
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateTid(String tid) {
        this.tid = tid;
    }
    public void updateStatus(UserStatus status) {
        this.userStatus = status;
    }

    // 사용자 권한을 ADMIN으로 변경할 수 있는 메서드
    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public User(String username, String password, String email, UserRole role) {
        this.userName = username;
        this.password = password;
        this.email = email;
        this.userRole= role;
    }

    public User(String username, String email, String password, Long kakaoId) {
        this.name = username;
        this.password = password;
        this.userName = email;
        this.email = email;
        this.kakaoId = kakaoId;
        this.userRole = UserRole.USER; // 기본적으로 USER로 권한 설정
        this.userStatus = UserStatus.MEMBER; // 처음 생성될때는 활성화 상태
        this.userGrade = UserGrade.BRONZE; // 처음 생성될 때는 브론즈
        this.mileage = 0L; // 처음 생성될 때는 0
    }

    public User kakaoIdUpdate(Long kakaoId) {
        this.kakaoId = kakaoId;
        return this;
    }
}
