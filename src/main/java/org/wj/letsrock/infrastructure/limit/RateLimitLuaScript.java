package org.wj.letsrock.infrastructure.limit;

public class RateLimitLuaScript {
    public static final String SLIDING_WINDOW_SCRIPT =
        "local key = KEYS[1] " +
        "local now = tonumber(ARGV[1]) " +
        "local window = tonumber(ARGV[2]) " +
        "local limit = tonumber(ARGV[3]) " +

        //清理过期的数据
        "redis.call('zremrangebyscore', key, 0, now - window) " +
        
        //统计当前窗口的请求数
        "local count = redis.call('zcount', key, now - window, now) " +
        
        // 是否允许本次请求
        "if count < limit then " +
        "  redis.call('zadd', key, now, now .. ':' .. math.random()) " +  // 添加随机后缀防止重复
        "  redis.call('expire', key, math.ceil(window/1000) * 2) " +      // 设置过期时间为窗口的2倍
        "  return 1 " +
        "end " +
        "return 0";
    public static final String BUCKET_SCRIPT =
            "local tokens_key = KEYS[1]\n" +
                    "local timestamp_key = KEYS[2]\n" +
                    "local rate = tonumber(ARGV[1])\n" +
                    "local capacity = tonumber(ARGV[2])\n" +
                    "local now = tonumber(ARGV[3])\n" +
                    "local requested = tonumber(ARGV[4])\n" +

                    "local last_tokens = tonumber(redis.call('get', tokens_key) or capacity)\n" +
                    "local last_refreshed = tonumber(redis.call('get', timestamp_key) or 0)\n" +
                    "local delta = math.max(0, now - last_refreshed)\n" +
                    "local filled_tokens = math.min(capacity, last_tokens + (delta * rate))\n" +

                    "if filled_tokens >= requested then\n" +
                    "    local new_tokens = filled_tokens - requested\n" +
                    "    redis.call('setex', tokens_key, 3600, new_tokens)\n" +
                    "    redis.call('setex', timestamp_key, 3600, now)\n" +
                    "    return 1\n" +
                    "else\n" +
                    "    return 0\n" +
                    "end";

}