package com.quickpick.ureca.OAuth.auth.config;

import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class TokenProviderOAuth {

    private final JwtPropertiesOAuth jwtProperties;

    public String generateToken(UserOAuth user, Duration expiredAt) {
        Date now = new Date();
        return makeToken(user, new Date( now.getTime() + expiredAt.toMillis()));
    } // expiredAt 만큼의 유효기간을 가진 토큰 생성

    public String makeToken(UserOAuth user, Date expiry) {

        return Jwts.builder()
                .issuer(jwtProperties.getIssuer())
                .expiration(expiry)
                .subject(user.getId())
                .claim("user_id", user.getUserId())
                .signWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .compact();
    }

    //토큰 검증 메서드
    public void validToken(String token) {
        try{
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseSignedClaims(token);
        } catch (SecurityException | MalformedJwtException e) { //서명이 불일치한 경우 / 구조가 손상된 경우
            throw new JwtException("Invalid JWT signature");
        } catch (ExpiredJwtException e) {                       //만료된 토큰인 경우 (이 경우에만 엑세스 토큰 재발급 요청)
            throw new JwtException("JWT token expired");
        } catch (UnsupportedJwtException e) {                   //지원하지 않는 토큰인 경우
            throw new JwtException("Unsupported JWT token");
        } catch (IllegalArgumentException e) {                  //토큰이 아예 없거나 비정상적으로 전달된 경우?
            throw new JwtException("JWT token is invalid");
        }
    }

    //토큰을 가지고 인증 객체 생성
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);
        Set<SimpleGrantedAuthority> authorities
                = Collections.singleton(new SimpleGrantedAuthority("ROLE_USER"));

        return new UsernamePasswordAuthenticationToken(
                new org.springframework.security.core.userdetails.User(
                        claims.getSubject(), "", authorities)
                , token
                , authorities);
    }

    //토큰 안의 userId 추출
    public Long getUserId(String token) {
        Claims claims = getClaims(token);
        return claims.get("user_id", Long.class);
    }

    //Claims 가져오기
    private Claims getClaims(String token) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();      //getBody()가 deprecated되어 이걸 쓸 것
    }

    //남은 토큰 유효시간 계산
    public long getRemainingValidity(String token) {
        Claims claims = getClaims(token);
        return claims.getExpiration().getTime() - System.currentTimeMillis();
    }
}
