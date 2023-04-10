package com.eunblog.api.config.data;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSession {
    public Long id;
    public String name;

    public UserSession(Long id) {
        this.id = id;
    }
}
