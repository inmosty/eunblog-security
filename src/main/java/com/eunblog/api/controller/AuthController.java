package com.eunblog.api.controller;

import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import com.eunblog.api.response.SessionResponse;
import com.eunblog.api.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        //json 아이디/비밀번호
        log.info("login = {}",login);

        //DB에서 조회
        String accessToken = authService.signin(login);

        //토콘을 응답
        return new SessionResponse(accessToken);
    }
}
