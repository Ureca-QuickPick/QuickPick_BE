package com.quickpick.ureca.OAuth.user.controller;

import com.quickpick.ureca.OAuth.user.dto.UserSignUpRequestOAuth;
import com.quickpick.ureca.OAuth.user.service.UserServiceOAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserControllerOAuth {

    private final UserServiceOAuth userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserSignUpRequestOAuth dto) {
        userService.saveUser(dto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return ResponseEntity.ok("테스트 성공");
    }

}
