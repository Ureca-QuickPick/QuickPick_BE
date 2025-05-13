package com.quickpick.ureca.config.jwt;

import com.quickpick.ureca.OAuth.auth.config.JwtPropertiesOAuth;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Builder;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
public class JwtFactory { //test용 jwt 토큰 생성
    private String subject = "test@email.com";
    private Date issuedAt = new Date();
    private Date expiration
            = new Date( new Date().getTime() + Duration.ofDays(14).toMillis() );
    private Map<String,Object> claims = Collections.emptyMap();

    @Builder
    public JwtFactory(String subject, Date issuedAt, Date expiration
            , Map<String,Object> claims) {
        this.subject = subject != null ? subject : this.subject;
        this.issuedAt = issuedAt != null ? issuedAt : this.issuedAt;
        this.expiration = expiration != null ? expiration : this.expiration;
        this.claims = claims != null ? claims : this.claims;
    }

    public static JwtFactory withDefaultValues() {

        return JwtFactory.builder().build();
    } // withDefaultValues

    public String createToken(JwtPropertiesOAuth jwtProperties) {
        // 기본 클레임 설정
        Map<String, Object> tokenClaims = new HashMap<>();

        // 표준 클레임 추가
        tokenClaims.put("sub", subject);                   // subject
        tokenClaims.put("iss", jwtProperties.getIssuer()); // issuer
        tokenClaims.put("iat", issuedAt);                  // issuedAt
        tokenClaims.put("exp", expiration);                // expiration

        // 사용자 정의 클레임 추가 (덮어쓰기 가능)
        if (claims != null && !claims.isEmpty()) {
            tokenClaims.putAll(claims);
        }

        return Jwts.builder()
                .claims(tokenClaims)
                .signWith(
                        Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8)),
                        Jwts.SIG.HS256 // 서명 알고리즘 명시 필수
                )
                .compact();
    }
}
