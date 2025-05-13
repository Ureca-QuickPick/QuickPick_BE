package com.quickpick.ureca.OAuth.auth.config;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class TokenAuthenticationFilterOAuth extends OncePerRequestFilter {
    private final TokenProviderOAuth tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;
    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String BEARER = "Bearer ";


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //요청 헤더의 auth 키의 값 조회
        String authHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authHeader);  //접두사 제거해서 토큰 가져오기

        try {
            if (token != null) {
                tokenProvider.validToken(token);                            //예외가 발생하면 catch문으로
                if (isBlacklisted(token)) {                                 //블랙리스트에 있는 토큰이면 예외 발생
                    throw new JwtException("Blacklisted token");
                }
                //토큰이 유효하고 블랙리스트에 없다면 인증 정보 설정
                Authentication auth = tokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response);
        } catch (JwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }
    //에러 메세지 설정 메서드
    private void setErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String responseBody = String.format("{\"error\": \"%s\"}", message);
        response.getWriter().write(responseBody);
    }

    private boolean isBlacklisted(String token) {
        //redis 내 블랙리스트에 있는지 검사
        return redisTemplate.hasKey("blacklist:" + token);
    }

    private String getAccessToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(BEARER)) {
            return authHeader.substring(BEARER.length());
        }
        return null;
    }

}
