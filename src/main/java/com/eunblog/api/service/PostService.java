package com.eunblog.api.service;

import com.eunblog.api.domain.Post;
import com.eunblog.api.domain.PostEditor;
import com.eunblog.api.exception.PostNotFound;
import com.eunblog.api.repository.PostRepository;
import com.eunblog.api.request.PostCreate;
import com.eunblog.api.request.PostEdit;
import com.eunblog.api.request.PostSearch;
import com.eunblog.api.response.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    public PostResponse get(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        return  PostResponse.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .build();

        /**
         * PostController -> WbPostService -> Repository
         *                   PostService
         * */

        //return response;
    }

/*    public List<PostResponse> getList() {
        return postRepository.findAll().stream().map(post->
            PostResponse.builder()
                    .id(post.getId())
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build()).collect(Collectors.toList());
    }*/
public List<PostResponse> getList(PostSearch postSearch) {

    //Pageable pageable = PageRequest.of(page,5, Sort.by(Sort.Direction.DESC,"id"));
    return postRepository.getList(postSearch).stream()
            .map(PostResponse::new)
            .collect(Collectors.toList());
    /*return postRepository.findAll(pageable).stream()
            .map(PostResponse::new)
            .collect(Collectors.toList());*/
}

    @Transactional
    public void edit(Long id, PostEdit postEdit) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);

        PostEditor.PostEditorBuilder editorBuilder = post.toEditor();

        log.info("postEdit.getTitle() = {}", postEdit.getTitle());
        log.info("postEdit.getContent() = {}", postEdit.getContent());

        PostEditor postEditor = editorBuilder.title(postEdit.getTitle())
                .content(postEdit.getContent())
                .build();

        log.info("postEditor = {}", postEditor);

        post.edit(postEditor);

        //return new PostResponse(post);
        //post.change(postEdit.getTitle(),postEdit.getContent());
//        postRepository.save(post);
    }

    @Transactional
    public void delete(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(PostNotFound::new);
        postRepository.delete(post);
    }


/*    public Post getRss(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 글입니다."));
        return post;
    }*/
}
