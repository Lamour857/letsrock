package org.wj.letsrock.infrastructure.limit;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Component
public class LocalRateLimiter {
    
    private final Cache<String, SlidingWindow> windowCache;
    
    public LocalRateLimiter() {
        this.windowCache = Caffeine.newBuilder()
                .expireAfterAccess(1, TimeUnit.HOURS)
                .build();
    }
    
    public boolean tryAcquire(String key, int limit, int period, TimeUnit unit) {
        try {
            SlidingWindow window = windowCache.get(key, k -> new SlidingWindow(limit, period, unit));
            return window.tryAcquire();
        } catch (Exception e) {
            log.error("Error in local rate limit for key: {}", key, e);
            return true; // 出现异常时放行
        }
    }
    
    private static class SlidingWindow {
        private final int limit;
        private final long windowSizeInMs;
        private final AtomicInteger counter;
        private volatile long windowStart;
        
        public SlidingWindow(int limit, int period, TimeUnit unit) {
            this.limit = limit;
            this.windowSizeInMs = unit.toMillis(period);
            this.counter = new AtomicInteger(0);
            this.windowStart = System.currentTimeMillis();
        }
        
        public synchronized boolean tryAcquire() {
            long currentTime = System.currentTimeMillis();
            
            // 检查是否需要滑动窗口
            if (currentTime - windowStart >= windowSizeInMs) {
                windowStart = currentTime;
                counter.set(0);
            }
            
            // 检查并增加计数
            if (counter.get() < limit) {
                counter.incrementAndGet();
                return true;
            }
            
            return false;
        }
    }
}