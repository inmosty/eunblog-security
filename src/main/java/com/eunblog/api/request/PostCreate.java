package com.eunblog.api.request;

import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;

@Slf4j
@Data
public class PostCreate {

    @NotBlank(message = "타이틀을 입력해주세요.")
    private String title;
    @NotBlank(message = "컨텐츠를 입력해주세요.")
    private String content;

    @Builder //생성자 위에 다는걸 추천
    public PostCreate(String title, String content) {
        this.title = title;
        this.content = content;
    }
    //빌더의 장점
    // - 가독성이 좋다.
    // - 값 생성에 대한 유연함
    // - 필요한 값만 받을 수 있다. // -> 오버로딩 가능한 조건 검색
    // - 객체의 불변성
}
