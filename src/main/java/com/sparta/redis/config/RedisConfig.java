package com.sparta.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration // Bean 설정
public class RedisConfig {
    @Bean // RedisTemplate Bean으로 등록, 의존성 주입
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>(); // RedisTemplate 객체 생성
        template.setConnectionFactory(connectionFactory); // Redis 연결 ConnectionFactory 설정
        // Redis 키 값 String으로 저장하게 설정.
        template.setKeySerializer(new StringRedisSerializer());
        // Redis 값을 JSON으로 직렬화해 저장하게 설정.
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        return template; // RedisTemplate 반환.
    }
}
