package org.wj.letsrock.infrastructure.lock;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.ExceptionUtil;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-12:56
 **/
@Aspect
@Component
@Slf4j
public class DistributedLockAspect {
    @Autowired
    private DistributedLock distributedLock;

    @Around("@annotation(distributedLocked)")
    public Object around(ProceedingJoinPoint point, DistributedLocked distributedLocked) throws Throwable {
        String lockKey = distributedLocked.key();
        if (StringUtils.isEmpty(lockKey)) {
            lockKey = generateKeyFromMethod(point);
        }

        boolean locked = false;
        try {
            locked = distributedLock.tryLock(lockKey,
                    distributedLocked.timeout(),
                    distributedLocked.timeUnit());

            if (!locked) {
                throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR,  "获取锁失败");
            }

            return point.proceed();
        } finally {
            if (locked) {
                distributedLock.unlock(lockKey);
            }
        }
    }

    private String generateKeyFromMethod(ProceedingJoinPoint point) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        return signature.getDeclaringTypeName() + ":" + signature.getName();
    }
}
