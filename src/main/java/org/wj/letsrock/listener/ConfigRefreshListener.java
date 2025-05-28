package org.wj.letsrock.listener;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.cloud.context.environment.EnvironmentChangeEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Service;
import org.wj.letsrock.infrastructure.config.dynamic.DatabasePropertySource;
import org.wj.letsrock.infrastructure.event.ConfigRefreshEvent;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:31
 **/
@Service
public class ConfigRefreshListener implements ApplicationListener<ConfigRefreshEvent> {
    @Autowired
    private ConfigurableEnvironment environment;  // 用于获取PropertySource
    @Autowired
    private ApplicationContext applicationContext; // 用于发布事件
    @Override
    public void onApplicationEvent(ConfigRefreshEvent event) {

        // 从环境中获取自定义的DatabasePropertySource
        PropertySource<?> propertySource = environment.getPropertySources().get("databaseProps");
        if (propertySource instanceof DatabasePropertySource) {
            DatabasePropertySource dbSource = (DatabasePropertySource) propertySource;

            // 强制刷新：重新从数据库加载最新配置
            dbSource.refresh();

            // 发布环境变更事件，触发@ConfigurationProperties更新
            applicationContext.publishEvent(
                    new EnvironmentChangeEvent(
                            Arrays.stream(dbSource.getPropertyNames()).collect(Collectors.toSet())
            ));

        }

    }
}
