package com.jojoldu.book.freelecspringboot3webservice.config.auth;

import com.jojoldu.book.freelecspringboot3webservice.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@EnableWebSecurity // 1. Spring Security 설정 활성화
@Configuration
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .headers().frameOptions().disable() // 2 h2-console 화면을 사용하기 위해 해당 옵션들 disable
                .and()
                .authorizeHttpRequests() // 3 Matchers 옵션을 사용하기 위해 선언해야 됨
                .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                .requestMatchers("/api/v1/**").hasRole(Role.USER.name()) // 4 permitAll() 전체 열람 권한, User권한 따로 분리
                .anyRequest().authenticated() // 5 any: 설정된 값 나머지 URL 관리, authenticated(): 인증된 사용자들에게만 허용
                .and()
                .logout()
                .logoutSuccessUrl("/") // 6 로그아웃 성공시 / 주소로 이동
                .and()
                .oauth2Login() // 7 OAuth2 로그인 기능에 대한 여러 설정의 진입점
                .userInfoEndpoint() // 8 OAuth2 로그인 성공 이후 사용자 정보를 가져올 때의 설정들을 담당
                .userService(customOAuth2UserService);// 9 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스의 구현체를 등록

        return http.build();

    }
}
