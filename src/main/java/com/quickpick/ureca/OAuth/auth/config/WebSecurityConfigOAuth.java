package com.quickpick.ureca.OAuth.auth.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfigOAuth {

    private final UserDetailsService userDetailsService;
    private final TokenProviderOAuth tokenProvider; // TokenProvider 추가
    private final RedisTemplate<String, String> redisTemplate;

    // Static 리소스는 인증 없이 접근
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (webSecurity) -> webSecurity.ignoring()
                .requestMatchers(new AntPathRequestMatcher("/static/**"));
    }

    // Security Filter Chain
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, OAuth2LoginSuccessHandlerOAuth oAuth2LoginSuccessHandler) throws Exception {
        return http
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))        //서버 세션 비활성화(jwt 사용하므로)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login", "/signup", "/auth/token", "/oauth2/**").permitAll()  // 로그인, 회원가입, 토큰 재발급, 소셜로그인은 인증 없이 접근
                        .anyRequest().authenticated()  // 그 외 요청은 인증 필요
                )
                .formLogin(AbstractHttpConfigurer::disable)             //폼로그인 비활성화
                .csrf(AbstractHttpConfigurer::disable)  // CSRF 보호 비활성화 (API 서버일 경우)
                .oauth2Login(oauth2 -> oauth2
                        .successHandler(oAuth2LoginSuccessHandler)  // 소셜로그인 설정
                )
                .addFilterBefore(new TokenAuthenticationFilterOAuth(tokenProvider, redisTemplate), UsernamePasswordAuthenticationFilter.class)  // JWT 필터 폼 로그인 필터 앞에 추가
                .build();
    }

    // AuthenticationManager 설정 (필요한가?)
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return new ProviderManager(authProvider);
    }

    // BCryptPasswordEncoder 설정
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
