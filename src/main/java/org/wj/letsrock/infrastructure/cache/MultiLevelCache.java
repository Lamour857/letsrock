package org.wj.letsrock.infrastructure.cache;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-27-15:10
 **/
@Primary
@Component
@Slf4j
public class MultiLevelCache implements CacheService {
    private final CacheService localCache;
    private final CacheService remoteCache;

    public MultiLevelCache(@Qualifier("caffeineCache") CacheService localCache,
                           @Qualifier("redisCache") CacheService remoteCache) {
        this.localCache = localCache;
        this.remoteCache = remoteCache;
    }

    @Override
    //@MdcDot(bizCode = "cache start get")
    public <T> Optional<T> get(String key, Class<T> type) {
        String traceId = MDC.get("traceId");

        try {
            MDC.put("cacheKey", key);
            MDC.put("cacheOperation", "GET");

            // 1. 查本地缓存
            Optional<T> localValue = localCache.get(key, type);
            if (localValue.isPresent()) {
                log.info("[Cache][Local][Hit] key={}, type={}", key, type.getSimpleName());
                //recordCacheMetrics("local", "hit");
                return localValue;
            }
            log.debug("[Cache][Local][Miss] key={}, type={}", key, type.getSimpleName());
            //recordCacheMetrics("local", "miss");

            // 2. 查Redis
            Optional<T> remoteValue = remoteCache.get(key, type);
            if (remoteValue.isPresent()) {
                log.info("[Cache][Remote][Hit] key={}, type={}", key, type.getSimpleName());
                ///recordCacheMetrics("remote", "hit");

                // 回填本地缓存
                log.debug("[Cache][Local][Backfill] key={}, value={}", key, remoteValue.get());
                localCache.put(key, remoteValue.get());
                return remoteValue;
            }
            log.debug("[Cache][Remote][Miss] key={}, type={}", key, type.getSimpleName());
            //recordCacheMetrics("remote", "miss");

            return Optional.empty();

        } catch (Exception e) {
            log.error("[Cache][Error] Failed to get value, key={}, type={}, error={}",
                    key, type.getSimpleName(), e.getMessage(), e);
            throw e;
        } finally {
            MDC.remove("cacheKey");
            MDC.remove("cacheOperation");
        }
    }

    @Override
    public void put(String key, Object value, long ttl, TimeUnit timeUnit) {
        // 本地缓存
        //localCache.put(key, value, ttl,  timeUnit);
        remoteCache.put(key, value, ttl,  timeUnit);
    }

    @Override
    public void put(String key, Object value) {
        localCache.put(key, value);
        remoteCache.put(key, value);
    }

    @Override
    public <T> Map<String, T> hGetAll(String key,  Class<T> clazz) {
        return remoteCache.hGetAll(key,  clazz);
    }

    @Override
    public void hIncrement(String key, String field, Integer value) {
        // 仅操作远程缓存，并使本地缓存失效
        remoteCache.hIncrement(key, field, value);
        localCache.remove(key);  // 使本地缓存失效保证一致性
    }

    @Override
    public List<ImmutablePair<String, Double>> zTopNScore(String key, int n) {
        // 直接委托给远程缓存
        return remoteCache.zTopNScore(key, n);
    }

    @Override
    public void zAdd(String key, Object value, long l) {
        remoteCache.zAdd(key, value, l);
    }

    @Override
    public Long sAdd(String key, Object... values) {
        // 操作远程缓存并失效本地
        Long result = remoteCache.sAdd(key, values);
        localCache.remove(key);
        return result;
    }

    @Override
    public Long sRemove(String key, Object... values) {
        Long result = remoteCache.sRemove(key, values);
        localCache.remove(key);
        return result;
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        // 直接查询远程缓存
        return remoteCache.sMembers(key, clazz);
    }

    @Override
    public <T> Optional<T> sGet(String s, Class<T> clazz) {
        return remoteCache.sGet(s, clazz);
    }

    @Override
    public Long sSize(String key) {
        return remoteCache.sSize(key);
    }

    @Override
    public void remove(String key) {
        long startTime = System.currentTimeMillis();
        try {
            MDC.put("cacheKey", key);
            MDC.put("cacheOperation", "REMOVE");

            // 同时删除本地和远程缓存
            long localStartTime = System.currentTimeMillis();
            localCache.remove(key);
            long localCost = System.currentTimeMillis() - localStartTime;
            log.debug("[Cache][Local][Remove] key={}, cost={}ms", key, localCost);

            long remoteStartTime = System.currentTimeMillis();
            remoteCache.remove(key);
            long remoteCost = System.currentTimeMillis() - remoteStartTime;
            log.debug("[Cache][Remote][Remove] key={}, cost={}ms", key, remoteCost);

            log.info("[Cache][Total][Remove] key={}, totalCost={}ms", key, System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("[Cache][Error] Failed to remove key={}, error={}, cost={}ms",
                    key, e.getMessage(), System.currentTimeMillis() - startTime, e);
            throw e;
        } finally {
            MDC.remove("cacheKey");
            MDC.remove("cacheOperation");
        }
    }

    @Override
    public Boolean acquire(String lockKey) {
        // 分布式锁仅通过远程缓存实现
        return remoteCache.acquire(lockKey);
    }

    @Override
    public boolean sIsMember(String setKey, Object userId) {
        // 直接查询远程缓存
        return remoteCache.sIsMember(setKey, userId);
    }

    @Override
    public Boolean tryAcquire(String key, int seconds) {
        // 带超时的分布式锁
        return remoteCache.tryAcquire(key, seconds);
    }

    @Override
    public Long increment(String countKey, long l) {
        localCache.remove(countKey);
        return remoteCache.increment(countKey, l);
    }

    @Override
    public void expire(String key, int period, TimeUnit timeUnit) {
        remoteCache.expire(key, period, timeUnit);
    }

    @Override
    public void zRemove(String key, Object value) {
        remoteCache.zRemove(key, value);
    }

    @Override
    public <T> void hPutAll(String key, Map<String, T> map) {
        remoteCache.hPutAll(key, map);
    }
}
