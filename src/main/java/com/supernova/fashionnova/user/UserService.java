package com.supernova.fashionnova.user;

import com.supernova.fashionnova.user.dto.SignupRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    /**
     *
     * @param requestDto
     * @throws DUPLICATED_USERNAME 중복된 아이디 일때
     * @throws
     */
    public void signup(SignupRequestDto requestDto) {

    }
}
