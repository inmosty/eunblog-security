package com.eunblog.api.service;

import com.eunblog.api.crypto.ScryptPasswordEncoder;
import com.eunblog.api.domain.User;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @AfterEach
    void clean(){
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원 가입 성공")
    public void test() {
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        //given
        Signup signup = Signup.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();

        //when
        authService.signup(signup);

        //then
        assertEquals(1,userRepository.count());

        User user = userRepository.findAll().iterator().next();
        assertEquals("inmosty@gmail.com",user.getEmail());
//        assertNotEquals("1234",user.getPassword());
       // assertTrue(encoder.matches("1234", user.getPassword()));
        assertNotNull(user.getPassword());
    }

}