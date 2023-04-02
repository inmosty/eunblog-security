package com.eunblog.api.service;

import com.eunblog.api.domain.Post;
import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.request.PostCreate;
import com.eunblog.api.request.PostSearch;
import com.eunblog.api.response.PostResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @BeforeEach
    void clean(){
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("글 작성")
    void test1() {
        //given
        PostCreate postCreate = PostCreate.builder()
                .title("제목입니다.")
                .content("내용입니다.")
                .build();

        //when
        postService.write(postCreate);

        //then
        assertEquals(1L,postRepository.count());
        Post post = postRepository.findAll().get(0);
        assertEquals("제목입니다.",post.getTitle());
        assertEquals("내용입니다.",post.getContent());
    }

    @Test
    @DisplayName("글 1개 조회")
    void test2() {
        //given
        Post post = Post.builder()
                .title("foo")
                .content("bar")
                .build();
        postRepository.save(post);

        // when
        PostResponse result = postService.get(post.getId());

        //then
        assertNotNull(result);
        assertEquals(1L,postRepository.count());
        assertEquals("foo",post.getTitle());
        assertEquals("bar",post.getContent());
    }

    @Test
    @DisplayName("글 여러개 조회")
    void test3() {
        //given
        List<Post> requestPosts = IntStream.range(0, 20)
                .mapToObj(i -> {
                    return Post.builder()
                            .title("foo"+i)
                            .content("bar"+i)
                            .build();
                }).collect(Collectors.toList());

        postRepository.saveAll(requestPosts);

        //sql -> select, limit, offset

        //Pageable pageable = PageRequest.of(0, 5,Sort.by(Sort.Direction.DESC,"id"));
        //Pageable pageable = PageRequest.of(0, 10, DESC,"id");
        //Pageable pageable = PageRequest.of(0, 5);
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(10L,posts.size());
        assertEquals("foo19",posts.get(0).getTitle());
        //assertEquals("호돌맨 제목 - 30",posts.get(0).getTitle());
        //assertEquals("호돌맨 제목 - 26",posts.get(4).getTitle());
    }
    @Test
    @DisplayName("글 여러개 조회")
    void test3_old() {
        //given
        postRepository.saveAll(List.of(
                Post.builder()
                        .title("foo1")
                        .content("bar1")
                        .build(),
                Post.builder()
                        .title("foo2")
                        .content("bar2")
                        .build()
        ));

        //Pageable pageable = PageRequest.of(0, 5, DESC,"id");
        PostSearch postSearch = PostSearch.builder()
                .page(1)
                .size(10)
                .build();
        // when
        List<PostResponse> posts = postService.getList(postSearch);

        //then
        assertEquals(2L,posts.size());
    }

}