package org.wj.letsrock.infrastructure.lock;

import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-12:51
 **/
public interface DistributedLock {
    boolean tryLock(String key, long timeout, TimeUnit unit);
    void unlock(String key);
    boolean isLocked(String key);
}
