package com.quickpick.ureca.v3.ticket.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class RedisStockRepositoryV3 {

    private final RedisTemplate<String, String> redisTemplate;
    private final String DECREASE_SCRIPT = """
            local key = KEYS[1]
            local decrease = tonumber(ARGV[1])
            local current = tonumber(redis.call('get', key))
            if current and current >= decrease then
                return redis.call('decrby', key, decrease)
            else
                return -1
            end
            """;

    private final String KEY_PREFIX = "ticket:";

    private String getKey(final Long ticketId) {
        return KEY_PREFIX + ticketId;
    }

    public Long decrease(final Long ticketId, final Long quantity) {
        return redisTemplate.execute(
                new DefaultRedisScript<>(DECREASE_SCRIPT, Long.class),
                List.of(getKey(ticketId)),
                String.valueOf(quantity)
        );
    }

    public void setTicket(final Long ticketId, final Long quantity) {
        redisTemplate.opsForValue().set(getKey(ticketId), String.valueOf(quantity));
    }

    public boolean isExisted(final Long ticketId) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(getKey(ticketId)));
    }

    public void increase(final Long ticketId, final Long quantity) {
        redisTemplate.opsForValue().increment(getKey(ticketId), quantity);
    }
}
