package com.eunblog.api.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

//@Configuration
@Data
@ConfigurationProperties("eunblog")
public class AppConfig {
/*    public Hello hello;

    @Data
    public static class Hello{
        public String name;
        public String home;
        public String hobby;
        public Long age;
    }*/
    private byte[] jwtKey;

    public void setJwtKey(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
    public byte[] getJwtKey() {
        return jwtKey;
    }
}
