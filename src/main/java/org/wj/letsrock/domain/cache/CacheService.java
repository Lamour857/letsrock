package org.wj.letsrock.domain.cache;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.wj.letsrock.domain.article.model.dto.CategoryDTO;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-27-12:40
 **/
public interface CacheService {
    <T> Optional<T> get(String key, Class<T> type);

    void put(String key, Object value, long expire, TimeUnit timeUnit);
    void put(String key, Object value);


    <T> Map<String, T> hGetAll(String key,  Class<T> clazz);

    void hIncrement(String key, String field, Integer value);


    List<ImmutablePair<String, Double>> zTopNScore(String key, int n);
    void zAdd(String key, Object value, long l);


    // Set operations
    Long sAdd(String key, Object... values);
    Long sRemove(String key, Object... values);
    <T> Set<T> sMembers(String key,  Class<T> clazz);
    <T> Optional<T> sGet(String s, Class<T> clazz);
//    Boolean sIsMember(String key, Object value);
    Long sSize(String key);

    void remove(String key);

    default String generateKey(String key){
        return CacheKey.BASE_KEY_PREFIX+key;
    }

    Boolean acquire(String lockKey);

    boolean sIsMember(String key, Object userId);

    Boolean tryAcquire(String key, int seconds);


    Long increment(String countKey, long l);

    void expire(String countKey, int period, TimeUnit timeUnit);


    void zRemove(String dirtyArticleStatistic, Long articleId);
}
