package com.eunblog.api.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * {
 *     "code":"400",
 *     "message":"잘못된 요청입니다."
 *     "validation" :{
 *         "title":"값을 입력해주세요"
 *     }
 * }
 *
 * */

@Getter
//@JsonInclude(value = NON_EMPTY)//빈값을 제외한다.
//@RequiredArgsConstructor
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Map<String,String> validation;

    @Builder
    public ErrorResponse(String code, String message,Map<String,String> validation) {
        this.code = code;
        this.message = message;
        this.validation = validation;
    }

    public void addValidation(String field, String errorMessage){
        this.validation.put(field, errorMessage);
    }
}
