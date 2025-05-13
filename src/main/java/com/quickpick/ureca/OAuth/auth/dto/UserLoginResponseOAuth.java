package com.quickpick.ureca.OAuth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserLoginResponseOAuth {                 //로그인 응답 dto
    private String accessToken;
    private String refreshToken;
}
