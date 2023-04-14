package com.eunblog.api.service;

import com.eunblog.api.crypto.ScryptPasswordEncoder;
import com.eunblog.api.domain.User;
import com.eunblog.api.exception.AlreadyExistsEmailException;
import com.eunblog.api.exception.InvalidSigninInformation;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import com.eunblog.api.request.Signup;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

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
    @Test
    @DisplayName("회원 가입시 중복된 이메일")
    public void test2() {
        //given
        User user = User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();
        userRepository.save(user);

        Signup signup = Signup.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();

        //when
        assertThrows(AlreadyExistsEmailException.class, () -> authService.signup(signup));
    }

    @Test
    @DisplayName("로그인 성공")
    public void test3() {
        //given
/*        Signup signup = Signup.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();*/
        ScryptPasswordEncoder encoder = new ScryptPasswordEncoder();
        User user = User.builder()
                .email("inmosty@gmail.com")
                .password(encoder.encrypt("1234"))
                .name("박종국")
                .build();
        userRepository.save(user);

       // authService.signup(signup);

        Login login = Login.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build();

        //when
        Long userId = authService.signin(login);

        //then
        assertNotNull(userId);
    }
    @Test
    @DisplayName("로그인시 비밀번호 틀림")
    public void test4() {
        //given
        Signup signup = Signup.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();

        authService.signup(signup);

        Login login = Login.builder()
                .email("inmosty@gmail.com")
                .password("12342")
                .build();

        //expected
        assertThrows(InvalidSigninInformation.class,()->authService.signin(login));
    }

}