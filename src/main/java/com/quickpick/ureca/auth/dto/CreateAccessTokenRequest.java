package com.quickpick.ureca.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenRequest {     //엑세스 토큰 생성 요청
    private String refreshToken;
}
