package com.supernova.fashionnova.domain.user;


import static com.supernova.fashionnova.global.security.JwtConstants.ACCESS_TOKEN_EXPIRATION;
import static com.supernova.fashionnova.global.security.JwtConstants.ACCESS_TOKEN_TYPE;
import static com.supernova.fashionnova.global.security.JwtConstants.REFRESH_TOKEN_EXPIRATION;
import static com.supernova.fashionnova.global.security.JwtConstants.REFRESH_TOKEN_TYPE;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.supernova.fashionnova.global.exception.CustomException;
import com.supernova.fashionnova.global.exception.ErrorType;
import com.supernova.fashionnova.global.security.JwtUtil;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Slf4j(topic = "KAKAO Login")
@Service
@RequiredArgsConstructor
public class KakaoService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final JwtUtil jwtUtil;

    public List<String> kakaoLogin(String code)
        throws JsonProcessingException, UnsupportedEncodingException {

        // 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getToken(code);
        log.info("인가코드 : " + code);

        // 2. 토큰으로 카카오 API 호출 : "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
        KakaoUserInfoDto kakaoUserInfo = getKakaoUserInfo(accessToken);

        // 3. 필요시에 회원가입
        User kakaoUser = registerKakaoUserIfNeeded(kakaoUserInfo);

        // 4. 블랙리스트 유저 상태 체크
        if (kakaoUser.getUserStatus() == UserStatus.BLOCKED_MEMBER) {
            log.info("차단된 회원입니다.");
            throw new CustomException(ErrorType.BAD_REQUEST_USER_STATUS_BLOCK);
        }

        // 5. JWT 토큰 반환
        /*String generateToken = jwtUtil.generateToken(kakaoUser.getUserName(), ACCESS_TOKEN_EXPIRATION, ACCESS_TOKEN_TYPE);*/

        String accessToken2 =
            jwtUtil.generateToken(kakaoUser.getUserName(), ACCESS_TOKEN_EXPIRATION, ACCESS_TOKEN_TYPE);
        String refreshToken2 =
            jwtUtil.generateToken(kakaoUser.getUserName(), REFRESH_TOKEN_EXPIRATION, REFRESH_TOKEN_TYPE);
        List<String> tokens = new ArrayList<>();
        tokens.add(URLEncoder.encode(accessToken2, "utf-8").replaceAll("\\+", "%20"));
        tokens.add(URLEncoder.encode(refreshToken2, "utf-8").replaceAll("\\+", "%20"));

        kakaoUser.updateRefreshToken(jwtUtil.substringToken(refreshToken2));
        userRepository.save(kakaoUser);

        return tokens;
    }

    private String getToken(String code) throws JsonProcessingException {

        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kauth.kakao.com")
            .path("/oauth/token")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        // HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "authorization_code");
        body.add("client_id", "6a0a695cf674d71866251ddbfd539f84");
        body.add("redirect_uri", "https://sparta.super-nova.store/users/kakao/callback");
        body.add("code", code);

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(body);

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        // HTTP 응답 (JSON) -> 액세스 토큰 파싱
        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());

        return jsonNode.get("access_token").asText();
    }

    private KakaoUserInfoDto getKakaoUserInfo(String accessToken) throws JsonProcessingException {

//        log.info("엑세스 토큰 : " + accessToken);
        // 요청 URL 만들기
        URI uri = UriComponentsBuilder
            .fromUriString("https://kapi.kakao.com")
            .path("/v2/user/me")
            .encode()
            .build()
            .toUri();

        // HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        RequestEntity<MultiValueMap<String, String>> requestEntity = RequestEntity
            .post(uri)
            .headers(headers)
            .body(new LinkedMultiValueMap<>());

        // HTTP 요청 보내기
        ResponseEntity<String> response = restTemplate.exchange(
            requestEntity,
            String.class
        );

        JsonNode jsonNode = new ObjectMapper().readTree(response.getBody());
        System.out.println(jsonNode);
        Long id = jsonNode.get("id").asLong();
        String nickname = jsonNode.get("properties").get("nickname").asText();
        String email = jsonNode.get("kakao_account").get("email").asText();

        log.info("카카오 사용자 정보: " + id + ", " + nickname + ", " + email);

        return new KakaoUserInfoDto(id, nickname, email);
    }

    private User registerKakaoUserIfNeeded(KakaoUserInfoDto kakaoUserInfo) {

        // DB 에 중복된 Kakao Id 가 있는지 확인
        Long kakaoId = kakaoUserInfo.getId();
        User kakaoUser = userRepository.findByKakaoId(kakaoId).orElse(null);

        if (kakaoUser == null) {
            // 카카오 사용자 email 동일한 email 가진 회원이 있는지 확인
            String kakaoEmail = kakaoUserInfo.getEmail();
            User sameEmailUser = userRepository.findByEmail(kakaoEmail).orElse(null);

            if (sameEmailUser != null) {
                kakaoUser = sameEmailUser;
                // 기존 회원정보에 카카오 Id 추가
                kakaoUser = kakaoUser.kakaoIdUpdate(kakaoId);
            } else {
                // 신규 회원가입
                // password: random UUID
                String password = UUID.randomUUID().toString();
                String encodedPassword = passwordEncoder.encode(password);

                // email: kakao email
                String email = kakaoUserInfo.getEmail();

                kakaoUser = new User(kakaoUserInfo.getUsername(), kakaoUserInfo.getEmail(),
                    encodedPassword, kakaoId);
            }

            userRepository.save(kakaoUser);
        }

        return kakaoUser;
    }

}
