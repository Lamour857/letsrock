package org.wj.letsrock.infrastructure.canal.handler;

import org.wj.letsrock.infrastructure.event.CanalMessage;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-09-12:30
 **/
public interface CanalMessageHandler {
    void handle(CanalMessage message);
    boolean supports(String tableName);
}
