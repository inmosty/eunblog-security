package com.eunblog.api.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;

@Slf4j
//@Setter
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    private String content;

    @Builder
    public Post(String title, String content) {
        this.title = title;
        this.content = content;
    }
    public String getTitle(){
        //서비스의 정책을 넣지마세요! 절대!!
        return this.title;
    }

    public PostEditor.PostEditorBuilder toEditor(){
        log.info("Post toEditor title = {}", title);
        return PostEditor.builder()
                .title(title)
                .content(content);
    }

    public void edit(PostEditor postEditor) {
        title = postEditor.getTitle();
        content = postEditor.getContent();
    }

/*    public void change(String title, String content) {
        this.title = title;
        this.content = content;
    }*/

}
