package com.project.sns.user.repository;

import com.project.sns.user.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {
    // 이 클래스 의 역할은 redis 에 user 를 캐싱하고, 캐시해서 데이터를 가져오는 역할.

    // 어떤 데이터를 캐싱할 때, 특히 Redis 에서는 ttl 을 걸어주는 것이 좋다. => 어떤 일정 시간이 지나면 expired 가 되도록 설정해주는 것이 좋다는 뜻. => Redis 공간을 활용적으로 사용하기 위해서.

    private final RedisTemplate<String, User> userRedisTemplate;
    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);


    public void setUser(User user) {
        String key = getKey(user.getEmail());
        log.info("Set User to Redis {} , {}", key, user); // set시 어떤 데이터가 set 되는지 확인하는 용도.
        userRedisTemplate.opsForValue().set(key, user, USER_CACHE_TTL);
    }

    public Optional<User> getUser(String email) {
        String key = getKey(email);
        User user = userRedisTemplate.opsForValue().get(key);
        log.info("Get data from Redis {} , {}", key, user);
        return Optional.ofNullable(user);
    }

    private String getKey(String email) {
        //Redis에서 key 값을 구성할때는 prefix 를 붙여주는 것이 좋다.
        return "USER:" + email;
    }

    public void deleteUser(User user) {
        String key = getKey(user.getEmail());
        userRedisTemplate.delete(key);
    }
}
