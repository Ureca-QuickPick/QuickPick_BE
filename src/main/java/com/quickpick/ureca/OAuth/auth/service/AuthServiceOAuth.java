package com.quickpick.ureca.OAuth.auth.service;

import com.quickpick.ureca.OAuth.auth.config.TokenProviderOAuth;
import com.quickpick.ureca.OAuth.auth.domain.RefreshTokenOAuth;
import com.quickpick.ureca.OAuth.auth.dto.UserLoginResponseOAuth;
import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import com.quickpick.ureca.OAuth.user.service.UserServiceOAuth;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthServiceOAuth {

    private final UserServiceOAuth userService;
    private final TokenProviderOAuth tokenProvider;
    private final RefreshTokenServiceOAuth refreshTokenService;
    private final RedisTemplate<String, String> redisTemplate;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    //jwt 로그인
    @Transactional
    public UserLoginResponseOAuth login(String id, String password) {
        UserOAuth user = userService.findById(id)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));

        if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {       //비밀번호 일치 검증
            throw new BadCredentialsException("Invalid password");
        }

        String accessToken = tokenProvider.generateToken(user, Duration.ofHours(2));
        String refreshToken = tokenProvider.generateToken(user, Duration.ofDays(14));           //로그인 성공 시 토큰 발급

        refreshTokenService.save(user.getUserId(), refreshToken);

        return new UserLoginResponseOAuth(accessToken, refreshToken);
    }

    // 토큰 추출 (Authorization 헤더에서 Bearer 제거)
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");                 //Authorization값을 가지는 헤더 가져오기
        if (authHeader != null && authHeader.startsWith("Bearer ")) {          
            return authHeader.substring(7);                             //앞에 Bearer 를 제거해 토큰 값만 가져오기
        }
        throw new RuntimeException("Missing or invalid Authorization header");
    }

    //로그아웃
    @Transactional
    public void logout(String accessToken) {

        //엑세스 토큰 블랙리스트 추가
        long expiration = tokenProvider.getRemainingValidity(accessToken);                //엑세스 토큰의 남은 유효시간 계산
        redisTemplate.opsForValue().set("blacklist:" + accessToken, "logout", expiration, TimeUnit.MILLISECONDS); //남은 유효시간 만큼 블랙리스트에 넣기

        //리프레시 토큰 삭제
        Long userId = tokenProvider.getUserId(accessToken);
        refreshTokenService.deleteByUserId(userId);
    }

    //리프레시 토큰을 이용한 엑세스 토큰 재발급
    @Transactional
    public String createNewAccessToken(String refreshToken) {
        //리프레시 토큰이 유효하지 않으면 에러
        try {
            tokenProvider.validToken(refreshToken);
        } catch (JwtException e) {
            throw new JwtException(e.getMessage());
        }

        //저장된 리프레시 토큰 값과 달라도 에러 (아마 위에서 다 걸리지겠지만 혹시 모르니까)
        RefreshTokenOAuth savedRefreshToken = refreshTokenService.findByRefreshToken(refreshToken);
        if (savedRefreshToken == null) {
            throw new JwtException("Invalid JWT RefreshToken");
        }
        
        //유효성이 검증되면 유저 정보 받아와서 새 엑세스 토큰 생성
        Long userId = savedRefreshToken.getUserId();
        UserOAuth user = userService.findByUserId(userId);

        return tokenProvider.generateToken(user, Duration.ofHours(2));
    }
}
