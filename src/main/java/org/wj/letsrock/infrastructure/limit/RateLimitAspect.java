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
    private SlidingWindowRateLimiter rateLimiter;
    
    // 添加 ExpressionParser 实例
    private final ExpressionParser parser = new SpelExpressionParser();
    
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint point, RateLimit rateLimit) throws Throwable {
        String key = generateKey(point, rateLimit);
        
        // 使用滑动窗口限流器
        if (!rateLimiter.tryAcquire(key, 
                rateLimit.limit(), 
                rateLimit.period(), 
                rateLimit.timeUnit())) {
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
        try {
            MethodSignature signature = (MethodSignature) point.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = point.getArgs();
            
            EvaluationContext context = new StandardEvaluationContext();

            for (int i = 0; i < paramNames.length; i++) {
                context.setVariable(paramNames[i], args[i]);
            }
            
            Expression expression = parser.parseExpression(spEl);
            return String.valueOf(expression.getValue(context));
        } catch (Exception e) {
            log.error("Failed to parse SpEL expression: {}", spEl, e);
            return spEl; // 解析失败时返回原始表达式
        }
    }
}