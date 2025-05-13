package com.quickpick.ureca.OAuth.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestOAuth {                  //로그인 요청 dto
    private String id;         // 사용자 ID
    private String password;   // 비밀번호
}
