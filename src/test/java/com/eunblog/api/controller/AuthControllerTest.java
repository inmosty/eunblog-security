package com.eunblog.api.controller;

import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.repository.UserRepository;
import com.eunblog.api.request.Signup;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @BeforeEach
    void clean() {
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("회원가입")
    public void test6() throws Exception {
        //given
        Signup signup = Signup.builder()
                .email("inmosty@gmail.com")
                .password("1234")
                .name("박종국")
                .build();

        //expected
        mockMvc.perform(post("/auth/signup")   // Content-Type -> application/json
                        .content(objectMapper.writeValueAsString(signup))
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andDo(print());
    }
}