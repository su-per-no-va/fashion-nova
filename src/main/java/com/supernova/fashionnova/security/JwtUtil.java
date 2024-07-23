package com.supernova.fashionnova.security;

import static com.supernova.fashionnova.security.JwtConstants.AUTHORIZATION_HEADER;
import static com.supernova.fashionnova.security.JwtConstants.BEARER_PREFIX;

import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.user.User;
import com.supernova.fashionnova.user.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Slf4j(topic = "JwtUtil")
@RequiredArgsConstructor
public class JwtUtil {

    private final UserRepository userRepository;

    // 로그 설정
    public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

    @Value("${jwt.secret.key}")
    private String secret_key;
    private Key key;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret_key);
        key = Keys.hmacShaKeyFor(bytes);
    }

    //토큰 생성 Access, Refresh
    public String generateToken(String username, Long expires, String tokenType) {
        Date date = new Date();
        return BEARER_PREFIX +
            Jwts.builder().setSubject(username)
                .claim(AUTHORIZATION_HEADER, tokenType)
                .setExpiration(new Date(date.getTime() + expires))
                .setIssuedAt(date)
                .signWith(SignatureAlgorithm.HS256, secret_key)
                .compact();
    }

    // JWT 토큰 substring
    public String substringToken(String tokenValue) {
        if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        logger.error("Not Found Token");
        throw new NullPointerException("Not Found Token");
    }

    public void addJwtToHeader(HttpServletResponse response, String token, String headerName) {
        response.setHeader(headerName, token);
    }


    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }


    //사용자에게서 토큰을 가져오기
    public String getAccessTokenFromRequest(HttpServletRequest req) {
        return getTokenFromRequest(req, AUTHORIZATION_HEADER);
    }

    //리프레시 토큰을 UserName 을통해 가져오기
    public String getRefreshTokenFromRequest(String userName) {
        User user = userRepository.findByUserName(userName)
            .orElseThrow(() -> new CustomException(ErrorType.NOT_FOUND_USER)
            );

        return user.getRefreshToken();
    }

    //HttpServletRequest 에서 Cookie Value  JWT 가져오기
    public String getTokenFromRequest(HttpServletRequest req, String headerName) {
        String token = req.getHeader(headerName);
        if (token != null && !token.isEmpty()) {
            return token;
        }
        return null;
    }

    // 토큰 검증
    public void validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }

    }

}
