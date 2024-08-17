package com.supernova.fashionnova.global.security;

public class JwtConstants {

    public static final String AUTHORIZATION_HEADER = "Authorization";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";
    public static final String ACCESS_TOKEN_TYPE = "access";
    public static final String REFRESH_TOKEN_TYPE = "refresh";

    // Access Token 만료시간 설정 (3시간)
    public static final long ACCESS_TOKEN_EXPIRATION = 3 * 60 * 60 * 1000L; // 3시간

    // Refresh Token 만료기간 설정(24시간)
    public static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60 * 1000L; // 24시간

}
