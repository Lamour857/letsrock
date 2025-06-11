package org.wj.letsrock.infrastructure.canal.annotation;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CanalTable {
    String value(); // 表名
}