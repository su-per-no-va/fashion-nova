package com.supernova.fashionnova.domain.user;

import com.supernova.fashionnova.admin.AdminService;
import com.supernova.fashionnova.domain.coupon.dto.CouponRequestDto;
import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import com.supernova.fashionnova.domain.user.dto.UserResponseDto;
import com.supernova.fashionnova.domain.user.dto.UserRoleResponseDto;
import com.supernova.fashionnova.domain.user.dto.UserUpdateRequestDto;
import com.supernova.fashionnova.domain.warn.Warn;
import com.supernova.fashionnova.domain.warn.WarnRepository;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.global.security.JwtUtil;
import com.supernova.fashionnova.global.security.RefreshToken;
import com.supernova.fashionnova.global.security.RefreshTokenRepository;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final WarnRepository warnRepository;
    private final PasswordEncoder passwordEncoder;
    private final AdminService adminService;
    private final JwtUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 유저 회원가입
     *
     * @param requestDto
     * @throws CustomException DUPLICATED_USERNAME 중복된 아이디 일때
     * @throws CustomException DUPLICATED_EMAIL 중복된 이메일 일때
     */
    public void signup(SignupRequestDto requestDto) {

        // 중복체크
        checkDuplicate(requestDto);
        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());

        User user = User.builder()
            .userName(requestDto.getUserName())
            .password(encodedPassword)
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .phone(requestDto.getPhone())
            .build();

        userRepository.save(user);

        giveWelcomeCoupon(user);
    }

    /**
     * 유저 로그아웃
     *
     * @param accessToken
     */
    public void logout(String accessToken) {
        // Access Token에서 userName 추출
        String userName = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(accessToken)).getSubject();
        RefreshToken refreshToken = new RefreshToken(userName, accessToken);
        // Redis에서 Refresh Token 삭제
        refreshTokenRepository.delete(refreshToken);

    }

    /**
     * 유저 회원탈퇴
     *
     * @param accessToken
     * @throws CustomException NOT_FOUND_USER
     */
    public void withdraw(String accessToken) {
        String userName = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(accessToken)).getSubject();
        User user = userRepository.findByUserName(userName).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_USER));
        user.updateStatus(UserStatus.NON_MEMBER);
        RefreshToken refreshToken = new RefreshToken(userName, accessToken);
        refreshTokenRepository.delete(refreshToken);
        userRepository.save(user);
    }

    /**
     * 유저 정보 조회(자신만 가능)
     *
     * @param user
     * @return UserResponseDto
     */
    public UserResponseDto getUser(User user) {

        return new UserResponseDto(user);
    }

    /**
     * 유저 정보 수정
     *
     * @param requestDto
     * @param user
     * @return UserResponseDto
     * @throws CustomException NOT_FOUND_USER 유저를 찾을 수 없을때
     */
    @Transactional
    public UserResponseDto updateUser(UserUpdateRequestDto requestDto, User user) {

        User updateUser = userRepository.findByUserName(user.getUserName())
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER));

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        updateUser.updateUser(requestDto, encodedPassword);

        return new UserResponseDto(user);
    }

    /**
     * 유저 경고 조회
     *
     * @param user
     * @return List<WarnResponseDto>
     */
    @Transactional
    public List<WarnResponseDto> getCautionList(User user) {

        List<Warn> warnList = warnRepository.findByUser(user);

        return warnList.stream()
            .map(WarnResponseDto::new)
            .collect(Collectors.toList());
    }

    /**
     * 유저 롤 가져오기
     *
     * @param user
     * @return UserRoleResponseDto
     */
    public UserRoleResponseDto getUserRole(User user) {

        return new UserRoleResponseDto(user.getUserRole());
    }

    private void checkDuplicate(SignupRequestDto requestDto) {

        // userName 중복체크
        if (userRepository.existsByUserName(requestDto.getUserName())) {
            throw new CustomException(ErrorType.DUPLICATED_USERNAME);
        }

        // email 중복체크
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new CustomException(ErrorType.DUPLICATED_EMAIL);
        }

    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new CustomException(ErrorType.NOT_FOUND_USER));
    }

    private void giveWelcomeCoupon(User user) {

        CouponRequestDto couponRequest = CouponRequestDto.builder()
            .userId(user.getId())
            .name("가입 축하 쿠폰")
            .period(new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000))
            .sale("10")
            .type("WELCOME")
            .build();

        adminService.addCoupon(couponRequest);
    }

}
