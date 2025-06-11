package org.wj.letsrock.infrastructure.lock;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-12:54
 **/
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLocked {
    String key() default "";  // 锁的key
    long timeout() default 30000;  // 超时时间，默认30秒
    TimeUnit timeUnit() default TimeUnit.MILLISECONDS;
}
