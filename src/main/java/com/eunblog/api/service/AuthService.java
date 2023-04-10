package com.eunblog.api.service;

import com.eunblog.api.domain.Session;
import com.eunblog.api.domain.User;
import com.eunblog.api.exception.InvalidSigninInformation;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    @Transactional
    public String signin(Login login) {
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
               .orElseThrow(InvalidSigninInformation::new);

        Session session = user.addSession();
        return session.getAccessToken();
    }
}
