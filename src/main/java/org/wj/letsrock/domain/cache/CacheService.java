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

    // Hash相关操作
//    <T> T hGet(String key, String hashKey, Class<T> type);
//    void hPut(String key, String hashKey, Object value);
    <T> Map<String, T> hGetAll(String key,  Class<T> clazz);
//    void hPutAll(String key, Map<String, Object> map);
//    void hDelete(String key, String... hashKeys);
//    boolean hExists(String key, String hashKey);
    void hIncrement(String key, String field, Integer value);

    // Sorted Set operations for ranking
//    void zAdd(String key, Object value, double score);
//    Set<Object> zRange(String key, long start, long end);
//    Set<Object> zRevRange(String key, long start, long end);
    List<ImmutablePair<String, Double>> zTopNScore(String key, int n);
//    Long zRank(String key, Object value);
//    Long zRevRank(String key, Object value);
//    Double zScore(String key, Object value);
//    Long zCount(String key, double min, double max);
//    void zIncrBy(String key, Object value, double increment);

    // Set operations
    Long sAdd(String key, Object... values);
    Long sRemove(String key, Object... values);
    <T> Set<T> sMembers(String key,  Class<T> clazz);
    <T> Optional<T> sGet(String s, Class<T> clazz);
//    Boolean sIsMember(String key, Object value);
    Long sSize(String key);
//    Set<Object> sIntersect(String... keys);
//    Set<Object> sUnion(String... keys);
//    Set<Object> sDiff(String... keys);
//    Object sPop(String key);
//    Set<Object> sRandMembers(String key, long count);
    void remove(String key);
//    boolean exists(String key);
    default String generateKey(String key){
        return CacheKey.BASE_KEY_PREFIX+key;
    }

    Boolean acquire(String lockKey);

    boolean sIsMember(String key, Object userId);

    Boolean tryAcquire(String key, int seconds);


}
