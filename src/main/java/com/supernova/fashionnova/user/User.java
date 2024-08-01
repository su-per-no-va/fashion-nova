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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addressList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Mileage> mileageList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Coupon> couponList = new ArrayList<>();

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

    public void updateStatus(UserStatus status) {
        this.userStatus = status;
    }

    // 사용자 권한을 ADMIN으로 변경할 수 있는 메서드
    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }
}
