package org.wj.letsrock.infrastructure.limit;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-31-20:13
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RateLimit {
    /**
     * 限流key前缀
     */
    String key() default "";

    /**
     * SpEL表达式，用于动态获取限流key
     */
    String spEl() default "";

    /**
     * 时间周期
     */
    int period() default 1;

    /**
     * 时间单位
     */
    TimeUnit timeUnit() default TimeUnit.SECONDS;

    /**
     * 限制次数
     */
    int limit() default 1;
}