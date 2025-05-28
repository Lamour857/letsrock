package org.wj.letsrock.infrastructure.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-15:56
 **/
@Component
public class RateLimiter {
    @Autowired
    private CacheService cacheService;

    public boolean tryAcquire(String key, int seconds) {
        Boolean absent = cacheService.tryAcquire(key, seconds);
        return absent != null && absent;
    }
}
