package com.quickpick.ureca.OAuth.user.service;

import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import com.quickpick.ureca.OAuth.user.dto.UserSignUpRequestOAuth;
import com.quickpick.ureca.OAuth.user.repository.UserRepositoryOAuth;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceOAuth {

    private final UserRepositoryOAuth userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    //자체 로그인 유저 저장
    public void saveUser(UserSignUpRequestOAuth dto) {
        userRepository.save(UserOAuth.builder()
                .id(dto.getId())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .age(dto.getAge())
                .gender(dto.getGender())
                .build());
    }

    @Transactional
    //구글 소셜 로그인 유저 저장
    public UserOAuth saveFromOAuth2(OAuth2User oAuth2User) {
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        return userRepository.save(UserOAuth.builder()           //age와 gender는 더미로 채우기
                .id(email)
                .password("SOCIAL_USER") // 비밀번호는 사용하지 않으므로 더미
                .name(name != null ? name : "소셜사용자")
                .age(0) // 추후 입력 받을 수 있도록 기본값(더미값 입력)
                .gender("unknown") // "male" / "female"도 가능 (더미값 입력)
                .build());
    }

    //user_id(고유 번호)로 유저 검색
    public UserOAuth findByUserId(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(()-> new IllegalArgumentException("User not found"));
    }

    //id(아이디)로 유저 검색
    public Optional<UserOAuth> findById(String id) {
        return userRepository.findById(id);
                //.orElseThrow(()-> new IllegalArgumentException("User not found")); -> 각 사용 위치에서 예외를 처리하도록 변경
    }
}
