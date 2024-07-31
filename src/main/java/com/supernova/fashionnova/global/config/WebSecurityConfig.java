package com.supernova.fashionnova.global.config;

import com.supernova.fashionnova.global.exception.CustomAccessDeniedHandler;
import com.supernova.fashionnova.security.JwtAuthenticationFilter;
import com.supernova.fashionnova.security.JwtAuthorizationFilter;
import com.supernova.fashionnova.security.JwtUtil;
import com.supernova.fashionnova.security.UserDetailsServiceImpl;
import com.supernova.fashionnova.user.UserRole;
import com.supernova.fashionnova.user.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    //password 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt = 비밀번호를 암호화 해주는 Hash 함수(강력한 암호화)
        return new BCryptPasswordEncoder();
    }

    private static final Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserService userService;

    public WebSecurityConfig(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService,
        AuthenticationConfiguration authenticationConfiguration, @Lazy UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationConfiguration = authenticationConfiguration;
        this.userService = userService;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
        throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil, userService);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.formLogin(AbstractHttpConfigurer::disable);
        http.httpBasic(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
            sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
        http.authorizeHttpRequests((authorizeHttpRequests) ->
            authorizeHttpRequests
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() //리소스 접근 허용
                .requestMatchers("/fonts/**").permitAll()
                .requestMatchers("/vendor/**").permitAll()
                .requestMatchers("/hamburgers/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/users/signup").permitAll() // 회원가입 허용
                .requestMatchers(HttpMethod.POST, "/users/login").permitAll() // 로그인 허용
                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()// 상품 검색 허용
                .requestMatchers(HttpMethod.GET, "/reviews/**").permitAll()// 상품별 리뷰 조회 허용
                .requestMatchers("/**","/login","/signup").permitAll()
                .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.getAuthority()) //권한이 Admin 인 유저만 접근가능
                .anyRequest().authenticated() // 그 외 모든 요청 인증처리

        );

        // 필터관리 (필터 작동 순서 지정)
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), JwtAuthorizationFilter.class);

        return http.build();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }


}
