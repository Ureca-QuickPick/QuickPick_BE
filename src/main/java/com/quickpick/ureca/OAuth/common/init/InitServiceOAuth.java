/*
package com.quickpick.ureca.OAuth.common.init;

import com.quickpick.ureca.OAuth.user.domain.UserOAuth;
import com.quickpick.ureca.OAuth.user.repository.UserRepositoryOAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InitServiceOAuth {
    //로그인 성능 테스트를 위한 userCount 만큼의 user 생성, 베포환경에서는 작동 안하도록 코드 주석처리

    private final UserRepositoryOAuth userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public String initialize(int userCount) {
        userRepository.deleteAll();

        List<UserOAuth> users = new ArrayList<>();

        for (int i = 1; i <= userCount; i++) {
            UserOAuth user = UserOAuth.builder()
                    .id("user" + i)
                    .password(bCryptPasswordEncoder.encode("pw" + i))
                    .name("name" + i)
                    .age(26)
                    .gender("M")
                    .build();

            users.add(user);
        }
        userRepository.saveAll(users);

        return "초기화 완료: 유저 " + userCount + "명 생성";
    }
}
*/
