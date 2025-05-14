package com.quickpick.ureca.OAuth.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSignUpRequestOAuth {             //회원가입 요청 dto
    private String id;         // 사용자 ID
    private String password;   // 비밀번호
    private String name;       // 이름
    private Integer age;       // 나이
    private String gender;     // 성별
}
