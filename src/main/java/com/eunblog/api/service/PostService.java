package com.eunblog.api.service;

import com.eunblog.api.domain.Post;
import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.request.PostCreate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostService {
    private final PostRepository postRepository;

    public void write(PostCreate postCreate){
        //postCreate ->Entity
        Post post = Post.builder()
                .content(postCreate.getContent())
                .title(postCreate.getTitle())
                .build();//new Post(postCreate.getTitle(), postCreate.getContent());
        postRepository.save(post);
    }

}
