package com.eunblog.api.service;

import com.eunblog.api.crypto.PasswordEncoder;
import com.eunblog.api.domain.Session;
import com.eunblog.api.domain.User;
import com.eunblog.api.exception.AlreadyExistsEmailException;
import com.eunblog.api.exception.InvalidSigninInformation;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import com.eunblog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Transactional
    public Long signin(Login login) {
/*        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
               .orElseThrow(InvalidSigninInformation::new);*/

        User user = userRepository.findByEmail(login.getEmail())
                .orElseThrow(InvalidSigninInformation::new);

        boolean matches = encoder.matches(login.getPassword(), user.getPassword());
        if (!matches) {
            throw new InvalidSigninInformation();
        }

        Session session = user.addSession();
       // return session.getAccessToken();
        return user.getId();
    }

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = encoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
