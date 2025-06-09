package org.wj.letsrock.infrastructure.canal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.wj.letsrock.infrastructure.canal.handler.CanalMessageHandler;
import org.wj.letsrock.infrastructure.event.CanalMessage;

@Slf4j
@Component
public class CanalMessageDispatcher {
    
    private final CanalHandlerRegistry canalHandlerRegistry;

    public CanalMessageDispatcher(CanalHandlerRegistry canalHandlerRegistry) {
        this.canalHandlerRegistry = canalHandlerRegistry;
    }

    public void dispatch(CanalMessage message) {
        String table = message.getTable();
        if (!canalHandlerRegistry.hasHandler(table)) {
            log.warn("未找到表 {} 的处理器", table);
            return;
        }

        CanalMessageHandler handler = canalHandlerRegistry.getHandler(table);
        try {
            handler.handle(message);
        } catch (Exception e) {
            log.error("处理Canal消息失败, table: {}, error: {}", table, e.getMessage(), e);
            throw e;
        }
    }
}