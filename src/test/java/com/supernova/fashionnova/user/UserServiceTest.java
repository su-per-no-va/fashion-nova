package com.supernova.fashionnova.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.user.dto.SignupRequestDto;
import com.supernova.fashionnova.warn.Warn;
import com.supernova.fashionnova.warn.dto.WarnRepository;
import com.supernova.fashionnova.warn.dto.WarnResponseDto;
import java.time.LocalDateTime;
import java.util.List;
import javax.imageio.IIOException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Nested;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * create on 2024/07/23 create by IntelliJ IDEA.
 *
 * <p> New Project. </p>
 *
 * @author HoChan Son (hcson)
 * @version 1.0
 * @since 1.0
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private WarnRepository warnRepository;


  @InjectMocks
  private UserService userService;

  @Nested
  class SignUpTest {
    @Test
    @DisplayName("성공 테스트")
    void signupTest1() {
      // given
      SignupRequestDto requestDto  = new SignupRequestDto("userName",
          "Test1234!@#$",
          "na",
          "test@teasd.com",
          "010-1234-5678");

      // when
//      when(userRepository.existsByUserName(anyString())).thenThrow(CustomException.class);

      // then
      assertDoesNotThrow(() -> userService.signup(requestDto));
    }


    @Test
    @DisplayName("실패 테스트")
    void signupTest2() {
      // given
      SignupRequestDto requestDto  = new SignupRequestDto("userName",
          "Test1234!@#$",
          "na",
          "test@teasd.com",
          "010-1234-5678");

      // when
      when(userRepository.existsByUserName(anyString())).thenThrow(CustomException.class);

      // then
      assertThrows(CustomException.class, () -> userService.signup(requestDto));
    }
  }




  @Test
  void logoutTest() {
    // given
    User user = Mockito.mock(User.class);

    // then
    assertDoesNotThrow(() -> userService.logout(user));
  }

  @Test
  void getCautionListTest() {
    // given
    User user = Mockito.mock(User.class);

    Warn warn = Mockito.mock(Warn.class);
    String detail = "detail";
    given(warn.getDetail()).willReturn(detail); // warn 의 getDetail 의 return 값 지정
    given(warn.getCreatedAt()).willReturn(LocalDateTime.now()); // warn 의 getCreatedAt 의 return 값 지정

    // when
    when(warnRepository.findByUser(any(User.class))).thenReturn(List.of(warn));

    // then
    List<WarnResponseDto> result = userService.getCautionList(user);

    assertThat(result).isNotEmpty(); // 개수가 비어있지 않은지 체크
    assertThat(result.get(0).getDetail()).isEqualTo(detail); // 원하는 값이 나오는지 체크
  }
}