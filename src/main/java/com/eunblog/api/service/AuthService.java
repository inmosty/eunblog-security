package com.eunblog.api.service;

import com.eunblog.api.crypto.ScryptPasswordEncoder;
import com.eunblog.api.domain.User;
import com.eunblog.api.exception.AlreadyExistsEmailException;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Signup;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class AuthService {
    private final UserRepository userRepository;

    public void signup(Signup signup) {
        Optional<User> userOptional = userRepository.findByEmail(signup.getEmail());
        if (userOptional.isPresent()) {
            throw new AlreadyExistsEmailException();
        }

        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        String encryptedPassword = encoder.encrypt(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
