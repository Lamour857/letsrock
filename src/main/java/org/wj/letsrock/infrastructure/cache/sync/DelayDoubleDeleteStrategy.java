package org.wj.letsrock.infrastructure.cache.sync;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheService;

import java.util.concurrent.Executor;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-01-22:15
 **/
@Component
@Slf4j
public class DelayDoubleDeleteStrategy implements CacheSyncStrategy{
    @Autowired
    protected CacheService  cacheService;

    @Autowired
    @Qualifier("asyncExecutor")
    protected Executor executor;


    public void sync(String key, Object value,DatabaseUpdater updater) {
        try {
            // 1. 删除缓存
            cacheService.remove(key);

            // 2. 更新数据库
            updater.update();

            // 3. 延迟双删
            executor.execute(() -> {
                try {
                    // 延迟500ms
                    Thread.sleep(500);
                    cacheService.remove(key);
                    log.info("Delay double delete cache key: {}", key);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Delay double delete interrupted", e);
                }
            });
        } catch (Exception e) {
            log.error("Cache sync failed for key: {}", key, e);
            throw new RuntimeException("Cache sync failed", e);
        }
    }

    @Override
    public void delete(String key) {

    }
}
