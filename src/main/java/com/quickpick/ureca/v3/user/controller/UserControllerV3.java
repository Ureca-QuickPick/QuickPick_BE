package com.quickpick.ureca.v3.user.controller;

import com.quickpick.ureca.v3.user.dto.UserCreateRequestV3;
import com.quickpick.ureca.v3.user.service.UserServiceV3;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/v3/users")
@RestController
@RequiredArgsConstructor
public class UserControllerV3 {

    private final UserServiceV3 userServiceV3;

    @PostMapping
    public void createUser(@RequestBody UserCreateRequestV3 request) {
        userServiceV3.save(request);
    }
}
