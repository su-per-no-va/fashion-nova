package com.supernova.fashionnova.user;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.security.UserDetailsImpl;
import com.supernova.fashionnova.user.dto.UserResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(UserController.class)  // UserController만 테스트하기 위해 Spring MVC 테스트 환경을 설정합니다.
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;  // MockMvc를 자동 주입하여 컨트롤러를 테스트할 때 HTTP 요청을 보내고 응답을 받을 수 있습니다.

  @Autowired
  private ObjectMapper objectMapper;  // JSON 직렬화 및 역직렬화를 위한 Jackson 라이브러리의 객체입니다.

  @MockBean
  private UserService service;  // UserService를 목(Mock) 객체로 만들어 UserController의 의존성을 주입합니다.

  private final String baseUrl = "/users";  // 테스트할 기본 URL을 설정합니다.

  @BeforeEach
  void setUp() {
    // 테스트 전에 매번 실행되는 설정 메서드입니다.

    // UserDetailsImpl 클래스의 목 객체를 만듭니다.
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    // userDetails.getUsername()이 호출되면 "user"를 반환하도록 설정합니다.
    given(userDetails.getUsername()).willReturn("user");

    // userDetails.getUser()가 호출되면 새로운 User 객체를 반환하도록 설정합니다.
    given(userDetails.getUser()).willReturn(new User());

    // 보안 컨텍스트를 설정합니다.
    SecurityContextHolder.setContext(new SecurityContextImpl());
    SecurityContextHolder.getContext().setAuthentication(
        // UsernamePasswordAuthenticationToken으로 인증 객체를 만듭니다.
        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
  }

  @Test
  @DisplayName("유저 조회 테스트")
  void getUserTest() throws Exception {
    // getUserTest 메서드는 getUser 엔드포인트를 테스트합니다.

    // UserResponseDto 클래스의 목 객체를 만듭니다.
    UserResponseDto userResponseDto = Mockito.mock(UserResponseDto.class);

    // userResponseDto.getUserName()이 호출되면 "usernmame"을 반환하도록 설정합니다.
    given(userResponseDto.getUserName()).willReturn("usernmame");

    // userResponseDto.getName()이 호출되면 "name"을 반환하도록 설정합니다.
    given(userResponseDto.getName()).willReturn("name");

    // userResponseDto.getEmail()이 호출되면 "email"을 반환하도록 설정합니다.
    given(userResponseDto.getEmail()).willReturn("email");

    // service.getUser()가 호출되면 userResponseDto를 반환하도록 설정합니다.
    when(service.getUser(any())).thenReturn(userResponseDto);

    // MockMvc를 사용하여 HTTP GET 요청을 보내고 기대하는 응답을 검증합니다.
    mockMvc.perform(get(baseUrl))
        .andExpectAll(
            status().isOk(),  // 응답 상태 코드가 200 OK인지 확인합니다.
            content().contentType(MediaType.APPLICATION_JSON),  // 응답 콘텐츠 타입이 JSON인지 확인합니다.
            jsonPath("$.userName").exists(),  // 응답 JSON에 userName 필드가 존재하는지 확인합니다.
            jsonPath("$.name").exists(),  // 응답 JSON에 name 필드가 존재하는지 확인합니다.
            jsonPath("$.email").exists()  // 응답 JSON에 email 필드가 존재하는지 확인합니다.
        );
  }
}