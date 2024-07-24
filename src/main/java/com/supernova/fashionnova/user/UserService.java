package com.supernova.fashionnova.user;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.user.dto.UserUpdateRequestDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnResponseDto;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WarnRepository warnRepository;

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

    /**
     * 유저 로그아웃
     *
     * @param user
     */
    public void logout(User user) {

        user.updateRefreshToken("");
        userRepository.save(user);

    }

    /**
     * 유저 회원탈퇴
     *
     * @param user
     */
    public void withdraw(User user) {

        user.updateStatus(UserStatus.NON_MEMBER);
        user.updateRefreshToken("");
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
     * 유저 경고 조회
     *
     * @param user
     * @return List<WarnResponseDto>
     */
    @Transactional
    public List<WarnResponseDto> getCautionList(User user) {

        // 테스트 경고 작성
//        Warn warn1 = new Warn("님 블랙 컨슈머임", user);
//         Warn warn2 = new Warn("님 엄청난 블랙 컨슈머임", user);
//        warnRepository.save(warn1);
//        warnRepository.save(warn2);
        List<Warn> warnList = warnRepository.findByUser(user);

        return warnList.stream()
            .map(WarnResponseDto::new)
            .collect(Collectors.toList());
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
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_USER)
            );

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        updateUser.updateUser(requestDto,encodedPassword);

        return new UserResponseDto(user);
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
