package com.eunblog.api.crypto;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PlainPasswordEncoder implements PasswordEncoder{


    @Override
    public String encrypt(String rawPassword) {
        return rawPassword;
    }

    @Override
    public boolean matches(String rawPassword, String encryptedPassword) {
        return rawPassword.equals(encryptedPassword);
    }
}
