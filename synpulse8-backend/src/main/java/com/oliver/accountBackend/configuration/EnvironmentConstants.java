package com.oliver.accountBackend.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class EnvironmentConstants {
    private Environment env;

    public static String KAFKA_TOPIC;

    @PostConstruct
    public void readEnv() {
        KAFKA_TOPIC = env.getProperty("kafka-topic");
    }

    @Autowired
    public void setEnv(Environment env) {
        this.env = env;
    }
}
