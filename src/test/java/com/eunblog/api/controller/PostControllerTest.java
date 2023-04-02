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
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
        assertEquals(1L, postRepository.count());

        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.", post.getTitle());
        assertEquals("내용입니다.", post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test4() throws Exception {
        //given
        Post post = Post.builder()
                .title("123456789012345")
                .content("bar")
                .build();
        postRepository.save(post);

        //클라이언트 요구사항
            //json 응답에서 title값 길이를 최대 10글자로 해주세요.

        //expected
        mockMvc.perform(get("/posts/{postId}",post.getId())
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(post.getId()))
                .andExpect(jsonPath("$.title").value("1234567890"))
                .andExpect(jsonPath("$.content").value("bar"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("호돌맨 제목 - "+i)
                            .content("내용 - "+i)
                            .build();
                }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //클라이언트 요구사항
        //json 응답에서 title값 길이를 최대 10글자로 해주세요.

        //expected
        mockMvc.perform(get("/posts?page=1&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(5)))
                //.andExpect(jsonPath("$.[0].id").value(30))
                .andExpect(jsonPath("$.[0].title").value("호돌맨 제목 - 30"))
                .andExpect(jsonPath("$.[0].content").value("내용 - 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("페이지를 0으로 요청하면 첫 페이지를 가져온다.")
    void test6() throws Exception {
        //given
        List<Post> requestPosts = IntStream.range(1, 31)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("호돌맨 제목 - "+i)
                            .content("내용 - "+i)
                            .build();
                }).collect(Collectors.toList());
        postRepository.saveAll(requestPosts);

        //클라이언트 요구사항
        //json 응답에서 title값 길이를 최대 10글자로 해주세요.

        //expected
        mockMvc.perform(get("/posts?page=0&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()",is(5)))
                .andExpect(jsonPath("$.[0].id").value(requestPosts.get(29).getId()))
                .andExpect(jsonPath("$.[0].title").value("호돌맨 제목 - 30"))
                .andExpect(jsonPath("$.[0].content").value("내용 - 30"))
                .andDo(print());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test5_old() throws Exception {
        //given
        Post post1 = postRepository.save(Post.builder()
                .title("title_1")
                .content("content1")
                .build());

        Post post2 = postRepository.save(Post.builder()
                .title("title_2")
                .content("content2")
                .build());

        //클라이언트 요구사항
        //json 응답에서 title값 길이를 최대 10글자로 해주세요.

        //expected
        mockMvc.perform(get("/posts?page=0&size=5")
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                /**
                 * {id:...,title:...}
                 * */
                /**
                 * [{id:...,title:...],{id:...,title:...}]
                 * */
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(jsonPath("$.[0].id").value(post2.getId()))
                .andExpect(jsonPath("$.[0].title").value("title_2"))
                .andExpect(jsonPath("$.[0].content").value("content2"))
                .andExpect(jsonPath("$.[1].id").value(post1.getId()))
                .andExpect(jsonPath("$.[1].title").value("title_1"))
                .andExpect(jsonPath("$.[1].content").value("content1"))
                .andDo(print());
    }
}