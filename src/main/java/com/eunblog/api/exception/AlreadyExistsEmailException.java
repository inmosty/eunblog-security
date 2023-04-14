package com.eunblog.api.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AlreadyExistsEmailException extends EunblogException {

    private final static String MESSAGE = "이미 가입된 이메일입니다.";
    public AlreadyExistsEmailException() {
        super(MESSAGE);
    }

    public AlreadyExistsEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
