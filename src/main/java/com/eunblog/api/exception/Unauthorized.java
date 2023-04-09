package com.eunblog.api.exception;

import lombok.extern.slf4j.Slf4j;

/**
 * status -> 401
 * */
@Slf4j
public class Unauthorized extends EunblogException{
    private static final String MESSAGE = "인증이 필요합니다.";
    public Unauthorized() {
        super(MESSAGE);
    }

    public Unauthorized(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }
}
