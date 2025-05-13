package com.quickpick.ureca.v3.user.service;

import com.quickpick.ureca.v3.user.constants.GenderV3;
import com.quickpick.ureca.v3.user.domain.UserV3;
import com.quickpick.ureca.v3.user.dto.UserCreateRequestV3;
import com.quickpick.ureca.v3.user.repository.UserRepositoryV3;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceV3 {

    private final UserRepositoryV3 userRepositoryV3;

    @Transactional
    public void save(final UserCreateRequestV3 request) {

        UserV3 userV3 = UserV3.builder()
                .age(request.age())
                .name(request.name())
                .genderV3(GenderV3.valueOf(request.gender()))
                .password(request.password())
                .build();

        userRepositoryV3.save(userV3);
    }
}
