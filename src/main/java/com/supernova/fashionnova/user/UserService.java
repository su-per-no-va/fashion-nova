package com.supernova.fashionnova.user;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    }

    /** 유저 로그아웃
     *
     * @param user
     */
    public void logout(User user) {
        user.updateRefreshToken("");
        userRepository.save(user);
    }

    /** 유저 회원탈퇴
     *
     * @param user
     */
    public void withdraw(User user) {
        user.updateStatus(UserStatus.NON_MEMBER);
        user.updateRefreshToken("");
        userRepository.save(user);
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



}
