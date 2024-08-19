package com.supernova.fashionnova.global.security;

import static com.supernova.fashionnova.global.security.JwtAuthenticationFilter.jwtExceptionHandler;
import static com.supernova.fashionnova.global.security.JwtConstants.ACCESS_TOKEN_HEADER;

import com.supernova.fashionnova.global.exception.ErrorType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
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

//        log.info("현재주소 : " + req.getRequestURL().toString());

        // AccessToken 가져온후 가공
        String accessToken = req.getHeader(ACCESS_TOKEN_HEADER);

//        log.info("Authorization Header : " + req.getHeader(ACCESS_TOKEN_HEADER));

        if (accessToken == null || accessToken.isBlank() || "null".equals(accessToken)) {
            filterChain.doFilter(req, res);
            return;
        }

        // JWT 토큰 substring
        accessToken = jwtUtil.substringToken(accessToken);

        try {
            // 검사
            checkAccessToken(res, accessToken);

            // 유저 정보 가져오기
            Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);

            // RefreshToken 검증 (로그 아웃시 리프레쉬 토큰 없음)
            String refreshToken = jwtUtil.getRefreshTokenFromRequest(accessTokenClaims.getSubject());
            if (refreshToken.isEmpty()) {
                jwtExceptionHandler(res, ErrorType.NOT_FOUND_REFRESH_TOKEN);
                return;
            }

            // 인증처리
            setAuthentication(accessTokenClaims.getSubject());

        } catch (ExpiredJwtException e) {
            log.info("Access Token이 만료되었습니다. Refresh Token을 사용하여 재발급 시도 중...");
            String username = e.getClaims().getSubject();
            String refreshToken = jwtUtil.getRefreshTokenFromRequest(username);

            if (refreshToken != null && jwtUtil.validateToken(refreshToken)) {
                String newAccessToken = jwtUtil.createAccessToken(username, JwtConstants.ACCESS_TOKEN_EXPIRATION, JwtConstants.ACCESS_TOKEN_TYPE);
                res.setHeader(ACCESS_TOKEN_HEADER, "Bearer " + newAccessToken);

                // 인증 처리
                setAuthentication(username);

            } else {
                log.error("Refresh Token이 유효하지 않거나 만료되었습니다.");
                jwtExceptionHandler(res, ErrorType.NOT_FOUND_REFRESH_TOKEN);
                return;
            }
        } catch (Exception e) {
            log.error("JWT 인증 과정에서 오류 발생: " + e.getMessage());
            jwtExceptionHandler(res, ErrorType.NOT_FOUND_TOKEN);
            return;

        }

        filterChain.doFilter(req, res);
        log.info("AuthorizationFilter End Status: " + String.valueOf(res.getStatus()));
        log.info("End of filter");

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

        // 공백 제거
        accessToken = accessToken.replaceAll("\\s", "");

//        log.info("====accessToken==== : " + accessToken);

        // Access 토큰 유효성 검사
        jwtUtil.validateToken(accessToken);

    }

}
