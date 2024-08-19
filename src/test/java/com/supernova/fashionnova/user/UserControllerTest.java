/*
package com.supernova.fashionnova.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.user.KakaoService;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserController;
import com.supernova.fashionnova.domain.user.UserGrade;
import com.supernova.fashionnova.domain.user.UserService;
import com.supernova.fashionnova.domain.user.UserStatus;
import com.supernova.fashionnova.domain.user.dto.SignupRequestDto;
import com.supernova.fashionnova.domain.user.dto.UserResponseDto;
import com.supernova.fashionnova.domain.warn.Warn;
import com.supernova.fashionnova.domain.warn.dto.WarnResponseDto;
import com.supernova.fashionnova.global.security.UserDetailsImpl;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)  // UserController만 테스트하기 위해 Spring MVC 테스트 환경을 설정합니다.
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;  // MockMvc를 자동 주입하여 컨트롤러를 테스트할 때 HTTP 요청을 보내고 응답을 받을 수 있습니다.

    @Autowired
    private ObjectMapper objectMapper;  // JSON 직렬화 및 역직렬화를 위한 Jackson 라이브러리의 객체입니다.

    @MockBean
    private UserService service;  // UserService를 목(Mock) 객체로 만들어 UserController의 의존성을 주입합니다.

    @MockBean
    private KakaoService kakaoService;

    private final String baseUrl = "/users";  // 테스트할 기본 URL을 설정합니다.
    UserDetailsImpl userDetails = Mockito.mock(UserDetailsImpl.class);

    @BeforeEach
    void setUp() {
        // 테스트 전에 매번 실행되는 설정 메서드입니다.

        // Given a mock UserDetailsImpl
        given(userDetails.getUsername()).willReturn("user");
        given(userDetails.getUser()).willReturn(new User(
            "testUSer",
            "Test1234!@",
            "테스트유저",
            "test@gmail.com",
            "010-1234-5678"
        ));

        // Set the security context
        SecurityContextHolder.setContext(new SecurityContextImpl());
        SecurityContextHolder.getContext().setAuthentication(
            new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    @Test
    @DisplayName("유저 조회 테스트")
    void getUserTest() throws Exception {
        // getUserTest 메서드는 getUser 엔드포인트를 테스트합니다.

        // UserResponseDto 클래스의 목 객체를 만듭니다.
        UserResponseDto userResponseDto = Mockito.mock(UserResponseDto.class);
        given(userResponseDto.getUserName()).willReturn("usernmame");
        given(userResponseDto.getName()).willReturn("name");
        given(userResponseDto.getEmail()).willReturn("email");
        //when
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

    @Test
    @DisplayName("회원가입 테스트")
    void signup() throws Exception {
        // given - 테스트를 위한 준비 단계
        SignupRequestDto requestDto = SignupRequestDto.builder()
            .userName("testUser")
            .password("Tset12345!@")
            .name("테스트 유저")
            .email("test@naver.com")
            .phone("010-1234-5678")
            .build();

        // UserService의 signup 메서드를 모킹하여 아무 동작도 하지 않도록 설정합니다.
        doNothing().when(service).signup(any(SignupRequestDto.class));

        //then
        mockMvc.perform(post(baseUrl + "/signup")
                .with(csrf())
                .content(objectMapper.writeValueAsString(requestDto))  // 회원가입 엔드포인트로 POST 요청을 보냅니다.
                .contentType(MediaType.APPLICATION_JSON))  // 요청 본문의 콘텐츠 타입을 JSON으로 설정합니다.
            .andExpect(status().isOk())  // 응답 상태 코드가 201 Created인지 확인합니다.
            .andExpect(content().string("회원 가입 성공"));  // 응답 본문이 "회원가입 성공"인지 확인합니다.
    }

    @Test
    @DisplayName("로그아웃 테스트")
    void logout() throws Exception {
        //given
        User user = userDetails.getUser();
        //when
        doNothing().when(service).logout(user);
        //then
        mockMvc.perform(post(baseUrl + "/logout").with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType("text/plain;charset=UTF-8"),
                content().string("로그아웃 성공")
            );
    }

    @Test
    @DisplayName("회원탈퇴 테스트")
    void withdraw() throws Exception {
        //given
        User user = userDetails.getUser();
        //when
        doNothing().when(service).withdraw(user);
        //then
        mockMvc.perform(put(baseUrl + "/withdraw").with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType("text/plain;charset=UTF-8"),
                content().string("회원 탈퇴 성공")
            );
    }

    @Test
    @DisplayName("유저 경고 조회")
    void getCautionList() throws Exception {
        //given
        User user = userDetails.getUser();
        List<WarnResponseDto> warnResponseDtoList = Arrays.asList(
            new WarnResponseDto(new Warn("경고1", user)),
            new WarnResponseDto(new Warn("경고2", user))
        );
        //when
        when(service.getCautionList(user)).thenReturn(warnResponseDtoList);
        //then
        mockMvc.perform(get(baseUrl + "/caution").with(csrf()))
            .andExpectAll(
                status().isOk(),
                content().contentType(MediaType.APPLICATION_JSON),
                jsonPath("$.length()").value(warnResponseDtoList.size()),
                jsonPath("$[0].detail").value("경고1"),
                jsonPath("$[1].detail").value("경고2")

            );

    }

    @Test
    @DisplayName("유저 정보 수정 조회")
    void updateUser() throws Exception {
        //given
        UserResponseDto userResponseDto = Mockito.mock(UserResponseDto.class);
        given(userResponseDto.getUserName()).willReturn("updateUserName");
        given(userResponseDto.getName()).willReturn("name");
        given(userResponseDto.getEmail()).willReturn("email");
        given(userResponseDto.getPhone()).willReturn("010-1234-5678");
        given(userResponseDto.getStatus()).willReturn(UserStatus.MEMBER);
        given(userResponseDto.getGrade()).willReturn(UserGrade.BRONZE);
        given(userResponseDto.getMileage()).willReturn(0L);
        //when
        when(service.getUser(any())).thenReturn(userResponseDto);

        // MockMvc를 사용하여 HTTP GET 요청을 보내고 기대하는 응답을 검증합니다.
        mockMvc.perform(get(baseUrl))
            .andExpectAll(
                status().isOk(),  // 응답 상태 코드가 200 OK인지 확인합니다.
                content().contentType(MediaType.APPLICATION_JSON),  // 응답 콘텐츠 타입이 JSON인지 확인합니다.
                jsonPath("$.userName").exists(),  // 응답 JSON에 userName 필드가 존재하는지 확인합니다.
                jsonPath("$.name").exists(),  // 응답 JSON에 name 필드가 존재하는지 확인합니다.
                jsonPath("$.email").exists(), // 응답 JSON에 email 필드가 존재하는지 확인합니다.
                jsonPath("$.phone").exists(), // 응답 JSON에 phone 필드가 존재하는지 확인합니다.
                jsonPath("$.status").exists(), // 응답 JSON에 status 필드가 존재하는지 확인합니다.
                jsonPath("$.grade").exists(), // 응답 JSON에 grade 필드가 존재하는지 확인합니다.
                jsonPath("$.mileage").exists() // 응답 mileage email 필드가 존재하는지 확인합니다.
            );

    }

}

 */