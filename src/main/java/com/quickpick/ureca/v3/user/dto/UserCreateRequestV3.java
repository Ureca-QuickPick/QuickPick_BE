package com.quickpick.ureca.v3.user.dto;

public record UserCreateRequestV3(
        String id,
        String password,
        String name,
        int age,
        String gender
) {
}
