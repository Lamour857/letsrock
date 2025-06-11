package org.wj.letsrock.infrastructure.security.annotation;

import java.lang.annotation.*;

/**
 * 用于标记匿名访问方法
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AnonymousAccess {
}