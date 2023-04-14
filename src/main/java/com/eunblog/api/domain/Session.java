package com.eunblog.api.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import static java.util.UUID.randomUUID;

@Slf4j
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accessToken;

    @ManyToOne
    private User user;

    @Builder
    public Session(User user) {
        this.accessToken = randomUUID().toString();
        this.user = user;
    }
}
