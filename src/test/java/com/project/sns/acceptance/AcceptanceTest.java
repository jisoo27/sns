package com.project.sns.acceptance;

import com.project.sns.user.domain.User;
import com.project.sns.util.DatabaseCleanup;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.Objects;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = DEFINED_PORT)
public class AcceptanceTest {

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @Autowired
    private RedisTemplate<String, User> redisTemplate;

    @BeforeEach
    public void setUp() {
        databaseCleanup.execute();
        Objects.requireNonNull(redisTemplate.getConnectionFactory())
                .getConnection()
                .flushAll();
    }
}
