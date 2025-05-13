package com.quickpick.ureca.OAuth.auth.service;

import com.quickpick.ureca.OAuth.auth.domain.RefreshTokenOAuth;
import com.quickpick.ureca.OAuth.auth.repository.RefreshTokenRepositoryOAuth;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class RefreshTokenServiceOAuth {
    private final RefreshTokenRepositoryOAuth refreshTokenRepository;

    public RefreshTokenOAuth findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    }

    //refresh 토큰 저장 (db 저장)
    @Transactional
    public void save(Long userId, String refreshToken) {
        RefreshTokenOAuth token = new RefreshTokenOAuth(userId, refreshToken);
        refreshTokenRepository.save(token);
    }

    //refresh 토큰 삭제
    @Transactional
    public void deleteByUserId(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
    }
}
