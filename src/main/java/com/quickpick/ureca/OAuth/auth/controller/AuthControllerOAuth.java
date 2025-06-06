package com.quickpick.ureca.OAuth.auth.controller;

import com.quickpick.ureca.OAuth.auth.dto.*;
import com.quickpick.ureca.OAuth.auth.service.AuthServiceOAuth;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;


@RestController
@RequiredArgsConstructor
public class AuthControllerOAuth {
    private final AuthenticationManager authenticationManager;
    private final AuthServiceOAuth authService;

    @PostMapping("/auth/login")                 //jwt를 이용한 자체 로그인
    public ResponseEntity<?> login(@RequestBody UserLoginRequestOAuth request) {
        try {
            UserLoginResponseOAuth response = authService.login(request.getId(), request.getPassword());
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException | BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Login failed: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An unexpected error occurred.");
        }
    }

    @GetMapping("/auth/oauth/login")            //oauth를 이용한 구글 소셜 로그인
    public ResponseEntity<Void> redirectToSpringSecurityLogin() {
        String redirectUrl = "/oauth2/authorization/google";    // Spring Security가 제공하는 OAuth2 로그인 엔드포인트

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectUrl));

        return new ResponseEntity<>(headers, HttpStatus.SEE_OTHER); // 303 See Other
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        String accessToken = authService.extractToken(request);
        authService.logout(accessToken);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/auth/token")                                        //jwt 엑세스 토큰 재발급
    public ResponseEntity<?> createNewAccessToken(
            @RequestBody CreateAccessTokenRequestOAuth request) {
        try {
            String newAccessToken
                    = authService.createNewAccessToken(request.getRefreshToken());

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new CreateAccessTokenResponseOAuth(newAccessToken));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new CreateAccessTokenErrorResponseOAuth(e.getMessage()));
        }
    }
}
