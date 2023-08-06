package com.project.sns.unit;

import com.project.sns.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Objects;

@SpringBootTest
public class UnitTest {

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @BeforeEach
    void setUp() { // redis cache 제거
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .flushAll();
    }
}
