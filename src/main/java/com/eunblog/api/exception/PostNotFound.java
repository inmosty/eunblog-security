package com.eunblog.api.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * status -> 404
 * */
@Slf4j
public class PostNotFound extends EunblogException{
    private static final String MESSAGE = "존재하지 않는 글입니다.";
    public PostNotFound() {
        super(MESSAGE);
    }

    public PostNotFound(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
