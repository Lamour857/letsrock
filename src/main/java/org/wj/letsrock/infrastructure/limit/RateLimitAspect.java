package org.wj.letsrock.infrastructure.limit;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.utils.EnvironmentUtil;
import org.wj.letsrock.utils.ExceptionUtil;

import java.lang.reflect.Method;

@Slf4j
@Aspect
@Order(1)
@Component
public class RateLimitAspect {
    
    @Autowired
    private CacheService cacheService;
    
    private final ExpressionParser parser = new SpelExpressionParser();

    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        String key = generateKey(point, rateLimit);
        
        // 尝试获取限流令牌
        if (!tryAcquireToken(key, rateLimit)) {
            throw ExceptionUtil.of(StatusEnum.TOO_MANY_REQUESTS);
        }
        
        return point.proceed();
    }
    
    private String generateKey(ProceedingJoinPoint point, RateLimit rateLimit) {
        StringBuilder key = new StringBuilder(CacheKey.RATE_LIMIT);

        // 添加注解中的key前缀
        if (!rateLimit.key().isEmpty()) {
            key.append(rateLimit.key()).append(":");
        }
        
        // 解析SpEL表达式
        if (!rateLimit.spEl().isEmpty()) {
            key.append(parseSpEl(point, rateLimit.spEl()));
        }
        
        return key.toString();
    }
    
    private String parseSpEl(ProceedingJoinPoint point, String spEl) {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        String[] paramNames = signature.getParameterNames();
        Object[] args = point.getArgs();
        
        EvaluationContext context = new StandardEvaluationContext();

        for (int i = 0; i < paramNames.length; i++) {
            context.setVariable(paramNames[i], args[i]);
        }
        
        Expression expression = parser.parseExpression(spEl);
        return String.valueOf(expression.getValue(context));
    }
    
    private boolean tryAcquireToken(String key, RateLimit rateLimit) {
        String countKey = key + ":count";

        if(!EnvironmentUtil.isPro()){
            log.info(countKey);
        }
        
        Long count = cacheService.increment(countKey, 1L);
        if (count == 1) {
            // 设置过期时间
            cacheService.expire(countKey, rateLimit.period(), rateLimit.timeUnit());
        }
        
        return count <= rateLimit.limit();
    }
}