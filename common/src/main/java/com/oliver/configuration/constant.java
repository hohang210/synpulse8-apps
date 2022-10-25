package com.oliver.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class constant {
    private Environment env;

    public static String JWT_KEY;

    public static String JWT_EXPIRATION_TIME;

    @PostConstruct
    public void readEnv() {
        JWT_KEY = env.getProperty("jwt.key");
        JWT_EXPIRATION_TIME = env.getProperty("jwt.expiration.time");
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
