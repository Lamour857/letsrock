package org.wj.letsrock.infrastructure.limit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheKey;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
@Slf4j
public class SlidingWindowRateLimiter {
    private final StringRedisTemplate redisTemplate;
    private final RedisScript<Long> rateLimitScript;

    @Autowired
    public SlidingWindowRateLimiter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.rateLimitScript = RedisScript.of(RateLimitLuaScript.SLIDING_WINDOW_SCRIPT, Long.class);
    }

    public boolean tryAcquire(String key, int limit, int windowSize, TimeUnit unit) {
        try {
            String redisKey = CacheKey.RATE_LIMIT + key;
            long now = System.currentTimeMillis();
            long windowInMillis = unit.toMillis(windowSize);
            
            // 执行Lua脚本
            Long result = redisTemplate.execute(
                rateLimitScript,
                Collections.singletonList(redisKey),  // KEYS参数
                String.valueOf(now),                  // ARGV[1]: 当前时间
                String.valueOf(windowInMillis),       // ARGV[2]: 窗口大小
                String.valueOf(limit)                 // ARGV[3]: 限制次数
            );
            
            return result != null && result == 1;
        } catch (Exception e) {
            log.error("Rate limit error for key: {}", key, e);
            return false; // 发生异常时,降级处理允许请求通过
        }
    }
}