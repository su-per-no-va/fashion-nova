package com.supernova.fashionnova.adimn;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnRequestDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    private final WarnRepository warnRepository;

    private static final int PAGE_SIZE = 30;

    /** 유저 전체조회
     *
     * @param page
     * @return List<UserResponseDto>
     * 사이즈는 30으로 고정해놨음
     */
    public List<UserResponseDto> getAllUsers(int page) {

        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        Page<User> userPage = userRepository.findAll(pageable);

        return userPage.stream()
            .map(UserResponseDto::new)
            .collect(Collectors.toList());
    }

    /** 유저 경고 등록
     *
     * @param requestDto
     * @throws CustomException NOT_FOUND_USER 유저Id로 유저를 찾을 수 없을 때
     */
    public void createCaution(WarnRequestDto requestDto) {

        User user = userRepository.findById(requestDto.getUserId())
            .orElseThrow(()-> new CustomException(ErrorType.NOT_FOUND_USER));

        Warn warn = new Warn(requestDto.getDetail(),user);
        warnRepository.save(warn);
    }


}
