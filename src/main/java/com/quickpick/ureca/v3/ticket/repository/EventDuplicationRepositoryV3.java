package com.quickpick.ureca.v3.ticket.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class EventDuplicationRepositoryV3 {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String KEY_PREFIX = "processed:";

    public boolean markIfNotProcessed(final Long userId) {
        String key = KEY_PREFIX + userId;
        return Boolean.TRUE.equals(redisTemplate.opsForValue()
                .setIfAbsent(key, "1", Duration.ofMinutes(5)));
    }
}
