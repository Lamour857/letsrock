package org.wj.letsrock.infrastructure.canal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.wj.letsrock.infrastructure.canal.annotation.CanalTable;
import org.wj.letsrock.infrastructure.canal.handler.CanalMessageHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CanalHandlerRegistry implements InitializingBean {

    private final ApplicationContext applicationContext;
    private final Map<String, CanalMessageHandler> handlerMap = new HashMap<>();

    public CanalHandlerRegistry(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void afterPropertiesSet() {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(CanalTable.class);
        beans.forEach((beanName, bean) -> {
            if (bean instanceof CanalMessageHandler) {
                CanalTable annotation = bean.getClass().getAnnotation(CanalTable.class);
                String tableName = annotation.value();
                handlerMap.put(tableName, (CanalMessageHandler) bean);
                log.info("注册Canal处理器: table={}, handler={}", tableName, bean.getClass().getSimpleName());
            }
        });
    }

    public CanalMessageHandler getHandler(String tableName) {
        return handlerMap.get(tableName);
    }

    public boolean hasHandler(String tableName) {
        return handlerMap.containsKey(tableName);
    }
}