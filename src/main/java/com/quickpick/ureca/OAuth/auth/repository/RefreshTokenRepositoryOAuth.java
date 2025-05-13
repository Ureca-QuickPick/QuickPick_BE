package com.quickpick.ureca.OAuth.auth.repository;

import com.quickpick.ureca.OAuth.auth.domain.RefreshTokenOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepositoryOAuth extends JpaRepository<RefreshTokenOAuth, Long> {
    Optional<RefreshTokenOAuth> findByUserId(Long userId);
    Optional<RefreshTokenOAuth> findByRefreshToken(String refreshToken);
    void deleteByUserId(Long userId);
}
