package com.eunblog.api.controller;

import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import com.eunblog.api.response.SessionResponse;
import com.eunblog.api.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserRepository userRepository;
    private final AuthService authService;
    private static final String KEY = "bjMPtA04oRHGotaP/5rQmejCSDVPvZYg0KUiFppZxlA=";

    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login) {
        //DB에서 조회
        Long userId = authService.signin(login);

        //  Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
/*
        byte[] encodedKey = key.getEncoded();
        String strKey = Base64.getEncoder().encodeToString(encodedKey);
*/

        //bjMPtA04oRHGotaP/5rQmejCSDVPvZYg0KUiFppZxlA=
        //log.info("strKey = {}", strKey);

        SecretKey secretKey = Keys.hmacShaKeyFor(Base64.getDecoder().decode(KEY));

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(secretKey)
                .compact();
        log.info("jws = {}", jws);

        return new SessionResponse(jws);
    }
    /*@PostMapping("/auth/login_cookie")
    public ResponseEntity<Object> login_cookie(@RequestBody Login login) {
        //DB에서 조회
        String accessToken = authService.signin(login);
        ResponseCookie cookie = ResponseCookie.from("SESSION", accessToken)
                .domain("localhost")    // todo 서버 환경에 따른 분리 필요
                .path("/")
                .httpOnly(true)
                .maxAge(Duration.ofDays(30))
                .sameSite("Strict")
                .build();

        log.info("cookie = {}",cookie.toString());// todo 꼭해야되냐

        return ResponseEntity.ok()
                .header(SET_COOKIE, cookie.toString()).build();
    }
    @PostMapping("/auth/login_old")
    public SessionResponse login_old(@RequestBody Login login) {
        //json 아이디/비밀번호
        log.info("login = {}",login);

        //DB에서 조회
        String accessToken = authService.signin(login);

        //토콘을 응답
        return new SessionResponse(accessToken);
    }*/
}
