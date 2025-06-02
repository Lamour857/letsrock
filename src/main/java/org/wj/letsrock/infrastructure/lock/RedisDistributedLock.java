package org.wj.letsrock.infrastructure.lock;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-12:51
 **/
@Slf4j
@Component
public class RedisDistributedLock implements DistributedLock{
    private static final String LOCK_PREFIX = "letsrock:lock:";
    private static final long DEFAULT_EXPIRE = 30000; // 30秒

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Override
    public boolean tryLock(String key, long timeout, TimeUnit unit) {
        String lockKey = LOCK_PREFIX + key;
        String threadId = String.valueOf(Thread.currentThread().getId());

        long millsToExpire = unit.toMillis(timeout);
        Boolean result = redisTemplate.opsForValue()
                .setIfAbsent(lockKey, threadId, Duration.ofMillis(millsToExpire));

        return Boolean.TRUE.equals(result);
    }

    @Override
    public void unlock(String key) {
        String lockKey = LOCK_PREFIX + key;
        String threadId = String.valueOf(Thread.currentThread().getId());

        String script =
                "if redis.call('get',KEYS[1]) == ARGV[1] then" +
                        "   return redis.call('del',KEYS[1]) " +
                        "else" +
                        "   return 0 " +
                        "end";

        redisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
                Collections.singletonList(lockKey), threadId);
    }

    @Override
    public boolean isLocked(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(LOCK_PREFIX + key));
    }
}
