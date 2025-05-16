/*
package com.quickpick.ureca.OAuth.common.init;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class InitControllerOAuth {
    //로그인 성능 테스트를 위한 userCount 만큼의 user 생성, 베포환경에서는 작동 안하도록 코드 전문 주석처리

    private final InitServiceOAuth initServiceOAuth;

    @PostMapping("/signup2")
    public String init( @RequestParam(defaultValue = "3000") int userCount ) {
        return initServiceOAuth.initialize(userCount);
    }
}
*/
