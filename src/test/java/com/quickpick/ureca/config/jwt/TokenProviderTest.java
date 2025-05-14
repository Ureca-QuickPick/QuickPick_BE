package com.quickpick.ureca.config.jwt;

import com.quickpick.ureca.OAuth.auth.config.JwtPropertiesOAuth;
import com.quickpick.ureca.OAuth.auth.config.TokenProviderOAuth;
import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import com.quickpick.ureca.OAuth.user.repository.UserRepositoryOAuth;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Date;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class TokenProviderTest {
    @Autowired
    private TokenProviderOAuth tokenProvider;
    @Autowired
    private UserRepositoryOAuth userRepository;
    @Autowired
    private JwtPropertiesOAuth jwtProperties;

    @DisplayName("토큰 생성 테스트")
    @Test
    void generateToken() {
        UserOAuth testUser = userRepository.save(UserOAuth.builder()
                .id("user@gmail.com")
                .password("password")
                .name("testUser")
                .age(12)
                .gender("male")
                .build());

        String token = tokenProvider.generateToken(testUser, Duration.ofDays(14));

        Long userId = Jwts.parser()
                .verifyWith( Keys.hmacShaKeyFor(
                        jwtProperties.getSecretKey().getBytes( StandardCharsets.UTF_8 ) ) )
                .build()
                .parseSignedClaims(token)
                .getPayload().get("user_id", Long.class);

        assertThat(userId).isEqualTo(testUser.getUserId());
    }

    @DisplayName("토큰 검증 테스트-실패")
    @Test
    void validateToken_fail() {
        String token = JwtFactory.builder()
                .expiration(new Date( new Date().getTime() - Duration.ofDays(7).toMillis() ))
                .build()
                .createToken(jwtProperties);

        boolean result;
        try {
            tokenProvider.validToken(token);

            result = true;
        } catch (JwtException e) {
            result = false;
        }
        assertThat(result).isFalse();
    }

    @DisplayName("토큰 검증 테스트-성공")
    @Test
    void validateToken_success() {
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        boolean result;
        try {
            tokenProvider.validToken(token);

            result = true;
        } catch (JwtException e) {
            result = false;
        }
        assertThat(result).isTrue();
    }

    @DisplayName("토큰으로 인증 정보 가져오기")
    @Test
    public void getAuthentication() {
        String userEmail = "user@gmail.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .build()
                .createToken(jwtProperties);

        Authentication authentication = tokenProvider.getAuthentication(token);

        assertThat( ( (UserDetails) authentication.getPrincipal() ).getUsername() )
                .isEqualTo(userEmail);
    } // getAuthentication

    @DisplayName("토큰으로 유저 ID를 가져오기 테스트")
    @Test
    public void getUserId() {
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("user_id", userId))
                .build()
                .createToken(jwtProperties);

        Long userIdByToken = tokenProvider.getUserId(token);

        assertThat(userIdByToken).isEqualTo(userId);
    } // getUserId
}
