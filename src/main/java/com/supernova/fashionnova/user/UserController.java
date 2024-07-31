package com.supernova.fashionnova.user;

import com.supernova.fashionnova.global.util.ResponseUtil;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.user.dto.UserUpdateRequestDto;
import com.supernova.fashionnova.warn.dto.WarnResponseDto;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * 유저 회원가입
     *
     * @param requestDto
     * @return "회원가입 성공"
     */
    @PostMapping("/signup")
    public String signup(@Valid @RequestBody SignupRequestDto requestDto,Model model) {

        userService.signup(requestDto);

        return "redirect:/login";
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

        UserResponseDto responseDto = userService.updateUser(requestDto,userDetails.getUser());

        return ResponseUtil.of(HttpStatus.OK, responseDto);
    }
}