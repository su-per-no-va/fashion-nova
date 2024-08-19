package com.supernova.fashionnova.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRepository;
import com.supernova.fashionnova.domain.user.UserService;
import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import com.supernova.fashionnova.domain.user.dto.UserUpdateRequestDto;
import com.supernova.fashionnova.domain.warn.Warn;
import com.supernova.fashionnova.domain.warn.WarnRepository;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.exception.CustomException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)  // Mockito를 사용하여 테스트를 실행하도록 설정합니다.
class UserServiceTest {

    @Mock
    private UserRepository userRepository;  // UserRepository를 목(Mock) 객체로 생성합니다.

    @Mock
    private PasswordEncoder passwordEncoder;  // PasswordEncoder를 목(Mock) 객체로 생성합니다.

    @Mock
    private WarnRepository warnRepository;  // WarnRepository를 목(Mock) 객체로 생성합니다.

    @InjectMocks
    private UserService userService;  // 목(Mock) 객체들이 주입된 UserService 인스턴스를 생성합니다.


    @Nested  // 내부 클래스를 테스트 그룹으로 묶습니다.
    @DisplayName("회원 가입 테스트")
    class SignUpTest {

        /*
        @Test
        @DisplayName("회원 가입 성공 테스트") // 테스트의 이름을 지정합니다.
        void signupTest1() {
            // given - 테스트를 위한 준비 단계
            SignupRequestDto requestDto = new SignupRequestDto("userName",
                "Test1234!@#$",
                "na",
                "test@teasd.com",
                "010-1234-5678");

            // when - 실제로 테스트할 동작을 지정합니다.
            // 이 경우, UserRepository의 existsByUserName 메서드를 호출하면 CustomException을 던지도록 설정합니다.
            // when(userRepository.existsByUserName(anyString())).thenThrow(CustomException.class);

            // then - 예상되는 결과를 검증합니다.
            assertDoesNotThrow(
                () -> userService.signup(requestDto));  // signup 메서드가 예외를 던지지 않는지 확인합니다.
        }
         */

        @Test
        @DisplayName("회원 가입 실패 테스트") // 테스트의 이름을 지정합니다.
        void signupTest2() {
            // given - 테스트를 위한 준비 단계
            SignupRequestDto requestDto = new SignupRequestDto("userName",
                "Test1234!@#$",
                "na",
                "test@teasd.com",
                "010-1234-5678");

            // when - 실제로 테스트할 동작을 지정합니다.
            // 이 경우, UserRepository의 existsByUserName 메서드를 호출하면 CustomException을 던지도록 설정합니다.
            when(userRepository.existsByUserName(anyString())).thenThrow(CustomException.class);

            // then - 예상되는 결과를 검증합니다.
            assertThrows(CustomException.class,
                () -> userService.signup(requestDto));  // signup 메서드가 CustomException을 던지는지 확인합니다.
        }
    }

    /*
    @Test
    @DisplayName("로그 아웃 테스트")
    void logoutTest() {
        // given - 테스트를 위한 준비 단계
        User user = Mockito.mock(User.class);  // User 클래스의 목(Mock) 객체를 생성합니다.

        // then - 예상되는 결과를 검증합니다.
        assertDoesNotThrow(() -> userService.logout(user));  // logout 메서드가 예외를 던지지 않는지 확인합니다.
    }
     */

    @Test
    @DisplayName("유저 경고 조회 테스트")
    void getCautionListTest() {
        // given - 테스트를 위한 준비 단계
        User user = Mockito.mock(User.class);  // User 클래스의 목(Mock) 객체를 생성합니다.

        Warn warn = Mockito.mock(Warn.class);  // Warn 클래스의 목(Mock) 객체를 생성합니다.
        String detail = "detail";
        given(warn.getDetail()).willReturn(detail);  // warn 객체의 getDetail 메서드가 "detail"을 반환하도록 설정합니다.
        given(warn.getCreatedAt()).willReturn(LocalDateTime.now());  // warn 객체의 getCreatedAt 메서드가 현재 시간을 반환하도록 설정합니다.

        // when - 실제로 테스트할 동작을 지정합니다.
        when(warnRepository.findByUser(any(User.class))).thenReturn(List.of(warn));  // warnRepository의 findByUser 메서드가 warn 객체 리스트를 반환하도록 설정합니다.

        // then - 예상되는 결과를 검증합니다.
        List<WarnResponseDto> result = userService.getCautionList(user);  // getCautionList 메서드를 호출하고 결과를 저장합니다.

        assertThat(result).isNotEmpty();  // 결과 리스트가 비어있지 않은지 확인합니다.
        assertThat(result.get(0).getDetail()).isEqualTo(detail);  // 결과 리스트의 첫 번째 요소의 detail 값이 예상된 값과 일치하는지 확인합니다.
    }

    /*
    @Test
    @DisplayName("회원 탈퇴 테스트")
    void withdraw() {
        //given
        User user = Mockito.mock(User.class);  // Mock User 객체를 사용합니다.
        user.updateRefreshToken("Bearer refreshToken");  // 초기 상태 설정
        user.updateStatus(UserStatus.MEMBER);  // 초기 상태 설정

        //when
        userService.withdraw(user);

        //then
        verify(user).updateStatus(UserStatus.NON_MEMBER);  // 상태 업데이트가 호출되었는지 검증합니다.
        verify(user).updateRefreshToken("");  // 리프레시 토큰 초기화가 호출되었는지 검증합니다.
        verify(userRepository).save(user);  // User 저장이 호출되었는지 검증합니다.
    }

     */

    @Test
    @DisplayName("유저 정보 수정")
    void updateUser() {
        //given
        User user = User.builder()
            .userName("testUser")
            .password(passwordEncoder.encode("fb112233"))
            .name("테스트 유저")
            .email("test@naver.com")
            .phone("010-1234-5678")
            .build();

        UserUpdateRequestDto requestDto = UserUpdateRequestDto.builder()
            .userName("updateUser")
            .password("test1234!")
            .name("수정된 테스트 유저")
            .email("test1@naver.com")
            .phone("010-5678-9012")
            .build();

        given(userRepository.findByUserName(user.getUserName())).willReturn(Optional.of(user));

        //when
        userService.updateUser(requestDto, user);

        //then
        assertThat(user.getUserName()).isEqualTo("updateUser");
        assertThat(user.getPassword()).isEqualTo(passwordEncoder.encode("test1234!"));
        assertThat(user.getName()).isEqualTo("수정된 테스트 유저");
        assertThat(user.getEmail()).isEqualTo("test1@naver.com");
        assertThat(user.getPhone()).isEqualTo("010-5678-9012");

    }

}
