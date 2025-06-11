package org.wj.letsrock;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.infrastructure.cache.RedisCache;

import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-28-15:14
 **/
@SpringBootTest
@Slf4j
public class CacheTest {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private RedisCache redisCache;
    @Test
    public void testRedisCache() {
        redisCache.put("RedisTime", "no time");

        redisCache.put("RedisNoTime", "time",  15000,  TimeUnit.MILLISECONDS);


    }
}
