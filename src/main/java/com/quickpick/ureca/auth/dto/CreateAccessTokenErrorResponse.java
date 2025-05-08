package com.quickpick.ureca.auth.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAccessTokenErrorResponse {       //엑세스 토큰 생성 중 에러 발생 시 응답 dto
    private String error;

    public CreateAccessTokenErrorResponse(String error) {
        this.error = error;
    }
}
