package com.project.sns.configuration;

import com.project.sns.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class RedisConfiguration {

    // 변경이 너무 잦은 데이터는 캐싱을 해도 소용이 없다.
    // 캐싱은 데이터베이스에 있는 데이터를 조금 더 접근비용이 적은 곳에 두어 가지오게 하는 것인데
    // 원본 데이터가 변경되면 캐싱한 데이터 또한 변경되어야 하기 때문에 select 가 발생되게 되기 때문에 변경이 너무 잦은 데이터들은 캐싱을 하는 것이 옳지 않은 선택이 될 수도 있다.
    // 접근이 자주되는 데이터를 캐싱하는 것이 좋다.
    // user 같은 경우 필터를 탈때, db 에서 체크하는 로직이 존재하기 때문에 => 이 부분을 캐싱한다면 db의 부하를 줄일 수 있다.

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        factory.afterPropertiesSet();
        return factory;
    }

    // RedisTemplate 이란 Redis commend 를 좀 더 편리하게 사용할 수 있도록 하는 클래스
    @Bean
    public RedisTemplate<String, User> userRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, User> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory); // 실제 Redis 서버의 정보를 어플리케이션이 알고 있어야 하는데 그 정보를 가지고 있는 것이 ConnectionFactory 이다.
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(User.class));
        return redisTemplate;
    }

}
