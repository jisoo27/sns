package com.project.sns.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import redis.embedded.RedisServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@TestConfiguration
public class TestRedisConfig {

    @Value("${spring.redis.port}")
    private int port;

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() {
        try {
            redisServer = RedisServer.builder()
                    .port(port)
                    .setting("maxmemory 256M") // 메모리 할당 직접 해주어야 함
                    .build();
            redisServer.start();
        } catch (Exception e) {
            e.printStackTrace(); // 테스트 레디스가 종료되지 않도록
        }
    }

    @PreDestroy
    public void stopRedis() {
        redisServer.stop();
    }
}
