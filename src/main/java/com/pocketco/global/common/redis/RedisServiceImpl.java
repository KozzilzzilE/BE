package com.pocketco.global.common.redis;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String PREFIX = "backend:submission:";
    private static final Duration TTL = Duration.ofMinutes(10);

    @Override
    public void saveTokens(String submissionId, List<String> tokens) {
        try {
            String value = objectMapper.writeValueAsString(tokens);
            redisTemplate.opsForValue().set(PREFIX + submissionId, value, TTL);
        } catch (Exception e) {
            throw new RuntimeException("Redis 저장 실패", e);
        }
    }

    @Override
    public List<String> getTokens(String submissionId) {
        String value = redisTemplate.opsForValue().get(PREFIX + submissionId);
        if (value == null) throw new RuntimeException("토큰이 만료되었거나 존재하지 않습니다.");

        try {
            return objectMapper.readValue(value, new TypeReference<List<String>>() {});
        } catch (Exception e) {
            throw new RuntimeException("Redis 조회 실패", e);
        }
    }

    @Override
    public void deleteTokens(String submissionId) {
        redisTemplate.delete(PREFIX + submissionId);
    }
}