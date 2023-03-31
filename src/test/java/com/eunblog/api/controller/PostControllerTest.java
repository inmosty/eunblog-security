package com.eunblog.api.controller;

import com.eunblog.api.domain.Post;
import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.request.PostCreate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest
@AutoConfigureMockMvc
@SpringBootTest
class PostControllerTest {
    @Autowired
    private ObjectMapper objectMapper; //필수 학습

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("/posts 요청시 hello world를 출력한다.")
    public void test() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        String param = objectMapper.writeValueAsString(request);

        System.out.println("param = " + param);

        // 글 제목
        // 글 내용
        // 사용자 id, user,level
        /***
         *{
         *  "title" : "xxx",
         *  "content" : "xxx",
         *  "user" : {
         *      "id": "xxx"
         *      ,"name":"xxxx"
         *  }
         *
         * */
        //expected
        mockMvc.perform(post("/posts")   // Content-Type -> application/json
                                .content("{\"title\":\"제목입니다.\",\"content\":\"내용입니다.\"}")
                        .contentType(APPLICATION_JSON)
                                .content(param)
                        //.param("title", "글제목입니다.")
                        //.param("content", "글내용입니다. 하하")
                )
                .andExpect(status().isOk())
                //.andExpect(content().string("{}"))
                .andExpect(content().string(""))
                .andDo(print());

    }
    @Test
    @DisplayName("/posts 요청시 title값은 필수이다.")
    public void test2() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다")
                .title("")
                .build();

        String param = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")   // Content-Type -> application/json
                                //.content("{\"title\":\"\",\"content\":\"내용입니다.\"}")
                                .content(param)
                        .contentType(APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest())
                //.andExpect(status().isOk())
                //.andExpect(content().string("{}"))
                .andExpect(jsonPath("$.code").value("400"))
                .andExpect(jsonPath("$.message").value("잘못된 요청입니다."))
                .andExpect(jsonPath("$.validation.title").value("타이틀을 입력해주세요."))
                .andDo(print());
    }
    @Test
    @DisplayName("/posts 요청시 DB 에 값이 저장된다.")
    public void test3() throws Exception {
        //given
        PostCreate request = PostCreate.builder()
                .content("내용입니다.")
                .title("제목입니다.")
                .build();

        String param = objectMapper.writeValueAsString(request);

        //expected
        mockMvc.perform(post("/posts")   // Content-Type -> application/json
                                //.content("{\"title\":\"제목입니다.\",\"content\":\"내용입니다.\"}")
                                .content(param)
                        .contentType(APPLICATION_JSON)
                )
                //.andExpect(status().isBadRequest())
                .andExpect(status().isOk())
                .andDo(print());

        // then
        assertEquals(1L,postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());

    }

}