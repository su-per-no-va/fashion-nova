package com.supernova.fashionnova.security;

import static com.supernova.fashionnova.security.JwtAuthenticationFilter.jwtExceptionHandler;
import static com.supernova.fashionnova.security.JwtConstants.ACCESS_TOKEN_HEADER;

import com.supernova.fashionnova.global.exception.ErrorType;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {


    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
        throws ServletException, IOException {


        log.info("현재주소 : " + req.getRequestURL().toString());
        // 다음 필터로 넘길 주소
        if (req.getRequestURL().toString().equals("http://localhost:8080/users/signup")
            || req.getRequestURL().toString().equals("http://localhost:8080/users/login")
            || req.getRequestURL().toString().equals("http://localhost:8080/products/product")
            || req.getRequestURL().toString().matches("http://localhost:8080/reviews/\\d+")) {
            filterChain.doFilter(req, res);
            return;
        }

        //AccessToken 가져온후 가공
        String accessToken = req.getHeader(ACCESS_TOKEN_HEADER);

        log.info("Authorization Header : " + req.getHeader("Authorization"));

//        String accessToken1 = jwtUtil.getAccessTokenFromRequest(req);
//        String accessToken2 = jwtUtil.getTokenFromRequest(req,JwtUtil.AUTHORIZATION_HEADER);

        if (accessToken == null || accessToken.isBlank() || "null".equals(accessToken)) {
            filterChain.doFilter(req, res);
            return;
        }
        // 검사
        checkAccessToken(res, accessToken);

        // JWT 토큰 substring
        accessToken = jwtUtil.substringToken(accessToken);
        log.info("----------------------------" + accessToken);

        // 유저 정보 가져오기
        Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);

        // RefreshToken 검증 (로그 아웃시 리프레쉬 토큰 없음)
        String refreshToken = jwtUtil.getRefreshTokenFromRequest(accessTokenClaims.getSubject());
        if (refreshToken.isEmpty()) {
            jwtExceptionHandler(res, ErrorType.NOT_FOUND_REFRESH_TOKEN);
            return;
        }

        // 인증처리
        try {
            setAuthentication(accessTokenClaims.getSubject());
        } catch (Exception e) {
            log.error(e.getMessage());
            return;
        }

        filterChain.doFilter(req, res);

    }


    // 인증 처리
    public void setAuthentication(String userName) {

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(userName);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);

    }

    // 인증 객체 생성
    private Authentication createAuthentication(String userName) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(userName);

        return new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities());

    }

    private void checkAccessToken(HttpServletResponse response, String accessToken) {

        // hasText : Null체크
        if (!StringUtils.hasText(accessToken)) {
            log.error("AccessToken이 없습니다.");
            jwtExceptionHandler(response, ErrorType.NOT_FOUND_TOKEN);
            return;
        }

        //공백 제거
        accessToken = accessToken.replaceAll("\\s", "");
        log.info("=========================================================================================" + accessToken);
        // Access 토큰 유효성 검사
        jwtUtil.validateToken(accessToken);

    }

}
