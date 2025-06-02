package org.wj.letsrock.infrastructure.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:53
 **/
@Component("redisCache")
@Slf4j
public class RedisCache implements CacheService {

    @Autowired
    private StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();




    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        try {
            String json = redisTemplate.opsForValue().get(generateKey(key));
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, type));
        } catch (IOException e) {
            log.error("json转换异常", e);
            return Optional.empty();
        }
    }

    @Override
    public void put(String key, Object value, long expire, TimeUnit timeUnit) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(generateKey(key), json, expire,  timeUnit);

        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"json转换异常");
        }
    }

    @Override
    public void put(String key, Object value) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(generateKey(key), json);
        } catch (JsonProcessingException e) {
            log.error("json转换异常", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"json转换异常");
        }
    }


    @Override
    public void hIncrement(String key, String field, Integer value) {
        redisTemplate.opsForHash().increment(generateKey(key), field, value);
    }

    @Override
    public List<ImmutablePair<String, Double>> zTopNScore(String key, int n) {
        Set<ZSetOperations.TypedTuple<String>> tuples = redisTemplate.opsForZSet()
                .reverseRangeWithScores(generateKey(key), 0, n - 1);
        if (tuples == null) {
            return Collections.emptyList();
        }
        return tuples.stream()
                .map(tuple -> ImmutablePair.of(tuple.getValue(), tuple.getScore()))
                .collect(Collectors.toList());
    }

    @Override
    public void zAdd(String key, Object value, long l) {
        try{
            String json=objectMapper.writeValueAsString(value);
            redisTemplate.opsForZSet().add(generateKey(key),json,l);
        }catch ( IOException e){
             log.warn("Failed to add to zSet: {}", e.getMessage());
        }
    }

    @Override
    public Long sAdd(String key, Object... values) {
        try {
            String[] jsonValues = Arrays.stream(values)
                    .map(value -> {
                        try {
                            return objectMapper.writeValueAsString(value);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("JSON转换失败", e);
                        }
                    })
                    .toArray(String[]::new);
            return redisTemplate.opsForSet().add(generateKey(key), jsonValues);
        } catch (RuntimeException e) {
            log.error("集合添加元素异常", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR, "集合操作异常");
        }
    }

    @Override
    public Long sRemove(String key, Object... values) {
        try {
            String[] jsonValues = Arrays.stream(values)
                    .map(value -> {
                        try {
                            return objectMapper.writeValueAsString(value);
                        } catch (JsonProcessingException e) {
                            throw new RuntimeException("JSON转换失败", e);
                        }
                    })
                    .toArray(String[]::new);
            return redisTemplate.opsForSet().remove(generateKey(key), (Object[]) jsonValues);
        } catch (RuntimeException e) {
            log.error("集合移除元素异常", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR, "集合操作异常");
        }
    }

    @Override
    public <T> Set<T> sMembers(String key,  Class<T> type) {
        return Objects.requireNonNull(redisTemplate.opsForSet().members(generateKey(key))).stream().map(json -> {
            try {
                return objectMapper.readValue(json, type);
            } catch (IOException e) {
                log.error("json转换异常", e);
                throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"json转换异常");
            }
        }).collect(Collectors.toSet());
    }

    @Override
    public <T> Optional<T> sGet(String s, Class<T> clazz) {
        return Objects.requireNonNull(redisTemplate.opsForSet().members(generateKey(s))).stream().map(json -> {
            try {
                return objectMapper.readValue(json, clazz);
            } catch (IOException e) {
                log.error("json转换异常", e);
                throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,"json转换异常");
            }
        }).findFirst();
    }

    @Override
    public Long sSize(String key) {
        return redisTemplate.opsForSet().size(generateKey(key));
    }

    @Override
    public void remove(String key) {
        redisTemplate.delete(generateKey(key));
    }

    @Override
    public <T> Map<String, T> hGetAll(String key, Class<T> clazz) {
        String actualKey = generateKey(key);
        Map<Object, Object> entries = redisTemplate.opsForHash().entries(actualKey);
        if (entries.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<String, T> resultMap = new HashMap<>(entries.size());
        for (Map.Entry<Object, Object> entry : entries.entrySet()) {
            String field = entry.getKey().toString();
            String jsonValue = (String) entry.getValue();
            try {
                T value = objectMapper.readValue(jsonValue, clazz);
                resultMap.put(field, value);
            } catch (IOException e) {
                log.error("JSON转换异常，字段: {}, 值: {}", field, jsonValue, e);
                throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR, "哈希数据解析失败");
            }
        }
        return Collections.unmodifiableMap(resultMap);

    }


    @Override
    public Boolean acquire(String lockKey) {
        return redisTemplate.opsForValue().setIfAbsent(generateKey(lockKey), "1");
    }

    @Override
    public boolean sIsMember(String key, Object value) {
        try {
            String jsonValue = objectMapper.writeValueAsString(value);
            return Boolean.TRUE.equals(
                    redisTemplate.opsForSet().isMember(generateKey(key), jsonValue)
            );
        } catch (JsonProcessingException e) {
            log.error("JSON转换异常", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR, "JSON转换异常");
        }
    }

    @Override
    public Boolean tryAcquire(String key, int seconds) {
        return redisTemplate.opsForValue().setIfAbsent(
                generateKey(key),
                "1",
                seconds,
                TimeUnit.SECONDS
        );
    }

    @Override
    public Long increment(String key, long l) {
        return redisTemplate.opsForValue().increment(generateKey(key), l);
    }

    @Override
    public void expire(String key, int period, TimeUnit timeUnit) {
         redisTemplate.expire(generateKey(key), period, timeUnit);
    }

    @Override
    public void zRemove(String key, Object articleId) {
        try{
            String json= objectMapper.writeValueAsString(articleId);
             redisTemplate.opsForZSet().remove(generateKey(key), json);
        }catch (JsonProcessingException e){
             log.warn("Failed to delete object from zSet: {}", e.getMessage());
        }
    }
}

