package com.eunblog;

import com.eunblog.api.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class EunblogApplication {

    public static void main(String[] args) {
        SpringApplication.run(EunblogApplication.class, args);
    }

}
