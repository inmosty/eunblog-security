package com.eunblog.api.controller;

import com.eunblog.api.domain.Post;
import com.eunblog.api.request.PostCreate;
import com.eunblog.api.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//SSR -> jsp, thymeleaf, mustache, freemarker
            //-> html rendering
    //SPA -> javascript + <-> API (JSON)
    //  vue -> vue+SSR = nuxt
    //  react -> react + SSR = next
@Slf4j
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    //Http Method
    //GET,POST,PUT,PATCH,DELETE,OPTIONS, HEAD, TRACE,CONNECT
    //글 등록(POST)
    @PostMapping("/posts")
    public void post(@RequestBody @Valid PostCreate request) throws Exception {
        log.info("params = {}",request);
        // Case1. 저장한 데이터 Entity -> response 로 응답하기
        // Case2. 저장한 데이터의 primary_id -> response 로 응답하기
        //      Client 에서는 수신한 id를 post조회한 api를 통해서 데이터를 수신받음
        // Case3. 응답 필요 없음 -> 클라이언트에서 모든 POST(글) 데이터 context를 잘 관리함.
        // Bad Case : 서버에서 -> 반드시 이렇게 할껍니다! 노노
                //-> 서버에서는 유연하게 대응.
                // -> 한번에 일괄적으로 잘 처리되는 케이스가 없다. -> 잘 관리하는 형태가 중요합니다.

        postService.write(request);
        //return Map.of();
    }

    @PostMapping("/posts_old")
    public Map<String,String> post_old(@RequestBody @Valid PostCreate params, BindingResult result) throws Exception {    // (@ModelAttribute PostCreate params){ (@RequestParam Map<String,String> params){ (@RequestParam String title,@RequestParam String content){
        //1. 매번 메서드마다 값을 검증 해야 한다
        //  > 개발자가 까먹을 수 있다.
        //  > 검증 부분에서 버그가 발생할 여지가 높다.
        //  > 지겹다
        // 2. 응답값에 HashMap -> 응답 클래스를 만들어주는게 좋다
        // 3. 여러개의 에러처리 힘들다
        // 4. 세번이상의 반복적인 작업은 피해야한다.
        // 5. 코드 && 개발에 관한 모든것
        log.info("params = {}",params);
        if (result.hasErrors()) {
            List<FieldError> fieldErrors = result.getFieldErrors();
            FieldError firstFieldError = fieldErrors.get(0);
            String fieldName = firstFieldError.getField(); //title
            String errorMessage = firstFieldError.getDefaultMessage();//에러메시지

            Map<String, String> error = new HashMap<>();
            error.put(fieldName, errorMessage);
            return error;

        }
        //데이터를 검증하는 이유
        //1.client 개발자가 깜박할수 있다. 실수로 값을 안보낼 수있다.
        //2. client bug로 값이 누락 될수 있다.
        //3. 외부에 나쁜 사람이 값을 임의로 조작해서 보낼수 있다.
        //4. 서버 개발자의 편안함을 위해서
        String title = params.getTitle();
        /*if (title == null || title.equals("")) {
            //1. 빡세다(노가다)
            //2. 개발팁 -> 무언가 3번이상 반복작업을 할때 내가 뭔가 잘못하고 있는건 아닐지 의심한다.
            //3. 누락가능성
            //4. 생각보다 검증해야 할게 많다(꼼꼼하지 않을 수 있다.)
            //5. 뭔가 개발자 스럽지 않다.
            throw new Exception("타이틀값이 없어요.");
        }*/
        String content =params.getContent();
/*        if (content == null || content.equals("")) {
            throw new Exception("컨텐츠 값이 없어요");
        }*/
        return Map.of();
    }
}
