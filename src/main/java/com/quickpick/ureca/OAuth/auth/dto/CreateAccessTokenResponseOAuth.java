package com.quickpick.ureca.OAuth.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateAccessTokenResponseOAuth {  //엑세스 토큰 생성 요청에 대한 응답
    private String accessToken;
}
