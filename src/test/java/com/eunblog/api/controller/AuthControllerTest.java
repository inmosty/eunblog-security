package com.eunblog.api.controller;

import com.eunblog.api.domain.Session;
import com.eunblog.api.domain.User;
import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.repository.SessionRepository;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Login;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {
    @Autowired
    private ObjectMapper objectMapper; //필수 학습

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("로그인 성공")
    public void test() throws Exception {
        //given
        userRepository.save(User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build();

        String param = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")   // Content-Type -> application/json
                        .content(param)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk() )
                .andDo(print());
    }
    @Test
    @Transactional
    @DisplayName("로그인 성공 세션 1개 생성")
    public void test2() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build();


        String param = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")   // Content-Type -> application/json
                        .content(param)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk() )
                .andDo(print());

        User loggedInUser = userRepository.findById(user.getId()).orElseThrow(
                RuntimeException::new
        );

        Assertions.assertEquals(1L,loggedInUser.getSessions().size());
    }
    @Test
    @DisplayName("로그인 성공 세션 응답")
    public void test3() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build());

        Login login = Login.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build();


        String param = objectMapper.writeValueAsString(login);

        //expected
        mockMvc.perform(post("/auth/login")   // Content-Type -> application/json
                        .content(param)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk() )
                .andExpect( jsonPath("$.accessToken",Matchers.notNullValue()))
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 후 권한이 필요한 페이지 접속한다.")
    public void test4() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")   // Content-Type -> application/json
                        .header("Authorization",session.getAccessToken())
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
    @Test
    @DisplayName("로그인 후 검증되지 않은 세션값으로 권한이 필요한 페이지에 접속할 수 없다.")
    public void test5() throws Exception {
        //given
        User user = userRepository.save(User.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .build());
        Session session = user.addSession();
        userRepository.save(user);

        //expected
        mockMvc.perform(get("/foo")   // Content-Type -> application/json
                        .header("Authorization",session.getAccessToken()+"other")
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isUnauthorized())
                .andDo(print());
    }
}