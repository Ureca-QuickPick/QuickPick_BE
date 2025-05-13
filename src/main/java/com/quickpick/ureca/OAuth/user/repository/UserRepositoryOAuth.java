package com.quickpick.ureca.OAuth.user.repository;

import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepositoryOAuth extends JpaRepository<UserOAuth, Long> {
    Optional<UserOAuth> findById(String id);             //id(아이디)로 사용자 정보 가져오기
    Optional<UserOAuth> findByUserId(Long userId);       //user_id(고유번호)로 사용자 정보 가져오기
}
