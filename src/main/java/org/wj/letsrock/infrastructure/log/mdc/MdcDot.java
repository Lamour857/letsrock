package org.wj.letsrock.infrastructure.log.mdc;

import java.lang.annotation.*;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-10:35
 **/
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MdcDot {
    String bizCode() default "";
}
