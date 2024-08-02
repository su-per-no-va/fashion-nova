package com.supernova.fashionnova.user;

import com.amazonaws.Response;
import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.review.Review;
import com.supernova.fashionnova.review.dto.ReviewResponseDto;
import com.supernova.fashionnova.security.JwtUtil;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.user.dto.UserUpdateRequestDto;
import com.supernova.fashionnova.warn.dto.WarnResponseDto;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @GetMapping("/test")
    public ResponseEntity<TestResponseDto> test() {
        log.info("In test");
        return ResponseUtil.of(HttpStatus.OK,
            TestResponseDto.builder()
                .testName("으아아아앙")
                .testCode(15L)
                .testContent("잠와")
                .testNaEun("프론트 전문가 킹갓갓킹")
            .build());
    }


    /**
     * 유저 회원가입
     *
     * @param requestDto
     * @return "회원가입 성공"
     */

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {

        userService.signup(requestDto);

        return ResponseUtil.of(HttpStatus.OK,"회원 가입 성공");
    }

    /**
     * 유저 로그아웃
     *
     * @param userDetails
     * @return "로그아웃 성공"
     */
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.logout(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, "로그아웃 성공");
    }

    /**
     * @param userDetails
     * @return "회원탈퇴 성공"
     */
    @PutMapping("/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal UserDetailsImpl userDetails) {

        userService.withdraw(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, "회원 탈퇴 성공");
    }

    /**
     * 유저 정보 조회(자신만 가능)
     *
     * @param userDetails
     * @return UserResponseDto
     */
    @GetMapping
    public ResponseEntity<UserResponseDto> getUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserResponseDto userResponseDto = userService.getUser(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, userResponseDto);
    }

    /**
     * 유저 경고 조회(자신만 가능)
     *
     * @param userDetails
     * @return
     */
    @GetMapping("/caution")
    public ResponseEntity<List<WarnResponseDto>> getCautionList(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        List<WarnResponseDto> responseDtoList = userService.getCautionList(userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDtoList);
    }

    /**
     * 유저 정보 수정(본인만 가능)
     *
     * @param requestDto
     * @param userDetails
     * @return
     */
    @PutMapping
    public ResponseEntity<UserResponseDto> updateUser(
        @Valid @RequestBody UserUpdateRequestDto requestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {

        UserResponseDto responseDto = userService.updateUser(requestDto, userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }

    /**
     * 카카오 소셜 로그인
     *
     * @param code
     * @param response
     */
    @GetMapping("/kakao/callback")
    public void kakaoLogin(@RequestParam String code, HttpServletResponse response)
        throws IOException {
            List<String> token = kakaoService.kakaoLogin(code);

            Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, token.get(0).substring(7));
            Cookie cookie2 = new Cookie(token.get(1), token.get(1).substring(7));
            cookie.setPath("/");
            response.addCookie(cookie);
            response.addCookie(cookie2);


            response.sendRedirect("/index.html");
    }
}