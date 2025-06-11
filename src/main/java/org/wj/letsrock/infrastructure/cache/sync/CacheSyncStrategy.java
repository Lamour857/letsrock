package org.wj.letsrock.infrastructure.cache.sync;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-01-22:14
 **/
public interface CacheSyncStrategy {
    /**
     * 同步缓存和数据库
     * @param key 缓存key
     * @param value 新值
     */
    void sync(String key, Object value,DatabaseUpdater updater);

    /**
     * 删除缓存和数据库中的数据
     * @param key 缓存key
     */
    void delete(String key);
}
