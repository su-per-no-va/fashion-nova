package com.supernova.fashionnova.user;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

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

        User user = User.builder()
            .userName(requestDto.getUserName())
            .password(requestDto.getPassword())
            .name(requestDto.getName())
            .email(requestDto.getEmail())
            .phone(requestDto.getPhone())
            .build();
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
