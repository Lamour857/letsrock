package org.wj.letsrock.infrastructure.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-13:45
 **/
@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
    private volatile static ApplicationContext context;
    private volatile static Environment environment;

    private static Binder binder;
    public static String getConfig(String key) {
        return environment.getProperty(key);
    }

    public static void publishEvent(ApplicationEvent event) {
        log.info("发布事件:" + event);
        context.publishEvent(event);
    }

    @Override
    public void setApplicationContext( ApplicationContext applicationContext) throws BeansException {
        SpringUtil.context = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringUtil.environment = environment;
        binder = Binder.get(environment);
    }
    /**
     * 配置绑定类
     */
    public static Binder getBinder() {
        return binder;
    }
}
