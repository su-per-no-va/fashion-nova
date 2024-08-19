package com.supernova.fashionnova.global.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.domain.user.User;
import com.supernova.fashionnova.domain.user.UserRepository;
import com.supernova.fashionnova.domain.user.UserService;
import com.supernova.fashionnova.domain.user.UserStatus;
import com.supernova.fashionnova.domain.user.dto.LoginRequestDto;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.global.exception.ExceptionDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Getter
@Slf4j(topic = "로그인 및 JWT생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response) throws AuthenticationException {

        if ("application/json".equals(request.getContentType())) {

            try {

                // 요청받은 json 을 객체 형태로 변환
                LoginRequestDto loginRequestDto =
                    objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

                // 확인용 로그
                log.info("Received login request: " +
                    loginRequestDto.getUserName() + "     " + loginRequestDto.getPassword());

                // LoginRequestDto 에서 아이디를 통해 유저 찾아오기
                User user = userRepository.findByUserName(loginRequestDto.getUserName())
                    .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER)
                    );

                // 탈퇴한 회원 예외 처리
                if (UserStatus.NON_MEMBER == user.getUserStatus()) {
                    log.info("탈퇴한 회원입니다.");
                    jwtExceptionHandler(response, ErrorType.BAD_REQUEST_USER_STATUS_NON);
                    return null;
                }

                // 차단된 회원 예외처리
                if (UserStatus.BLOCKED_MEMBER == user.getUserStatus()) {
                    log.info("차단된 회원입니다.");
                    jwtExceptionHandler(response, ErrorType.BAD_REQUEST_USER_STATUS_BLOCK);
                    return null;
                }

                // 인증 요청 설정
                UsernamePasswordAuthenticationToken authRequest =
                    new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUserName(), loginRequestDto.getPassword());

                // 추가적인 요청정보를 authRequest 에 설정
                setDetails(request, authRequest);

                // authentication manager 를 통해 인증 시도
                return this.getAuthenticationManager().authenticate(authRequest);

            } catch (IOException e) {

                log.error("attemptAuthentication 예외 발생 {} ", e.getMessage());
                throw new RuntimeException(e);

            }

        }

        // 요청 json 형식이 아닐 경우 부모클래스의 기본 동작 수행
        return super.attemptAuthentication(request, response);

    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult) throws IOException {

        log.info("로그인 성공 및 JWT 토큰 생성");
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        String accessToken =
            jwtUtil.createAccessToken(user.getUserName(), JwtConstants.ACCESS_TOKEN_EXPIRATION, JwtConstants.ACCESS_TOKEN_TYPE);
        String refreshToken =
            jwtUtil.createAccessToken(user.getUserName(), JwtConstants.REFRESH_TOKEN_EXPIRATION, JwtConstants.REFRESH_TOKEN_TYPE);

        // 헤더에 전달해야 함
        jwtUtil.addJwtToHeader(response, accessToken, JwtConstants.ACCESS_TOKEN_HEADER);
        jwtUtil.addJwtToHeader(response, refreshToken, JwtConstants.REFRESH_TOKEN_HEADER);

        /*
        // 쿠키에 전달
        Cookie cookie = new Cookie(JwtUtil.AUTHORIZATION_HEADER, accessToken.substring(7));
        cookie.setPath("/");
        response.addCookie(cookie);
         */

        // 헤더에 userId 전달
        response.setHeader("userName", user.getUserName());

        // refresh 토큰을 Entity 에 저장
        // user.updateRefreshToken(jwtUtil.substringToken(refreshToken));
        jwtUtil.createRefreshToken(user.getUserName(), JwtConstants.REFRESH_TOKEN_TYPE);
        userRepository.save(user);

        // 로그인 메세지 띄우기
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("\"로그인 성공!!\"");

    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException failed) throws IOException {

        log.info("로그인 실패!!");

    }

    // 필터 예외처리
    public static void jwtExceptionHandler(HttpServletResponse response, ErrorType error) {

        response.setStatus(error.getHttpStatus().value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        try {
            String json = new ObjectMapper().writeValueAsString(new ExceptionDto(error));
            response.getWriter().write(json);
            throw new CustomException(error);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }

}