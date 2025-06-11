package org.wj.letsrock.infrastructure.limit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

import static org.wj.letsrock.infrastructure.limit.RateLimitLuaScript.BUCKET_SCRIPT;

@Component
public class TokenBucketRateLimiter {


    @Autowired
    private StringRedisTemplate redisTemplate;
    
    public boolean tryAcquire(String key, int permits) {
        List<String> keys = Arrays.asList(
            "tokens:" + key,
            "timestamp:" + key
        );
        
        return redisTemplate.execute(
            new DefaultRedisScript<>(BUCKET_SCRIPT, Long.class),
            keys,
            "10",           // rate
            "100",         // capacity
            String.valueOf(System.currentTimeMillis()),
            String.valueOf(permits)
        ) == 1;
    }
}