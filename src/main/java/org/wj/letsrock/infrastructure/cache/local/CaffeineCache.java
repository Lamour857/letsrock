package org.wj.letsrock.infrastructure.cache.local;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheService;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-27-15:03
 **/
@Component("caffeineCache")
@Slf4j
public class CaffeineCache implements CacheService {
    private final com.github.benmanes.caffeine.cache.Cache<String, Object> cache;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public CaffeineCache(@Value("${cache.local.maximum-size:10000}") long maximumSize,
                         @Value("${cache.local.expire-after-write:30m}") Duration expireAfterWrite) {
        this.cache = Caffeine.newBuilder()
                .maximumSize(maximumSize)
                .expireAfterWrite(expireAfterWrite)
                .build();
    }
    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        try {
            Object value = cache.getIfPresent(key);
            return Optional.ofNullable(value)
                    .map(v -> objectMapper.convertValue(v, type));
        } catch (Exception e) {
            log.error("Caffeine get error", e);
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, Object value, long expire,  TimeUnit timeUnit) {
        throw new UnsupportedOperationException("Caffeine cache does not support hash operations");
    }

    @Override
    public void put(String key, Object value) {
        try {
            // Caffeine的写入需要处理序列化
            Object serializableValue = objectMapper.convertValue(value, Object.class);
            cache.put(key, serializableValue);
        } catch (IllegalArgumentException e) {
            log.error("Value serialization failed", e);
        }
    }

    @Override
    public <T> Map<String, T> hGetAll(String key,  Class<T> clazz) {
        throw new UnsupportedOperationException("Caffeine cache does not support hash operations");
    }

    @Override
    public void hIncrement(String key, String field, Integer value) {
        throw new UnsupportedOperationException("Caffeine cache does not support hash operations");
    }

    @Override
    public List<ImmutablePair<String, Double>> zTopNScore(String key, int n) {
        throw new UnsupportedOperationException("Caffeine cache does not support sorted set operations");
    }

    @Override
    public Long sAdd(String key, Object... values) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public Long sRemove(String key, Object... values) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public <T> Set<T> sMembers(String key, Class<T> clazz) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public <T> Optional<T> sGet(String s, Class<T> clazz) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public Long sSize(String key) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public void remove(String key) {
        cache.invalidate(key);
    }

    @Override
    public Boolean acquire(String lockKey) {
        throw new UnsupportedOperationException("Caffeine cache does not support locking");
    }

    @Override
    public boolean sIsMember(String articleWhiteList, Object userId) {
        throw new UnsupportedOperationException("Caffeine cache does not support set operations");
    }

    @Override
    public Boolean tryAcquire(String key, int seconds) {
        throw new UnsupportedOperationException("Caffeine cache does not support locking");
    }

    @Override
    public Long increment(String countKey, long l) {
        throw new UnsupportedOperationException("Caffeine cache does not support increment operations");
    }

    @Override
    public void expire(String countKey, int period, TimeUnit timeUnit) {
        throw new UnsupportedOperationException("Caffeine cache does not support expire operations");
    }
}
