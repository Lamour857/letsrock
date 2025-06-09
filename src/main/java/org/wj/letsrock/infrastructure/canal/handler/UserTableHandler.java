package org.wj.letsrock.infrastructure.canal.handler;

import groovy.transform.AutoImplement;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.infrastructure.canal.annotation.CanalTable;
import org.wj.letsrock.infrastructure.event.CanalMessage;
import org.wj.letsrock.infrastructure.persistence.es.repository.ArticleEsRepository;

@Slf4j
@Component
@CanalTable("user")
public class UserTableHandler implements CanalMessageHandler {
    @Autowired
    private ArticleEsRepository  articleEsRepository;
    @Override
    public void handle(CanalMessage message) {
        log.info("处理用户表变更: {}", message);
        String type = message.getType();
        switch (type){
            case "INSERT":
                log.info("新增用户: {}", message.getData());
                break;
            case "UPDATE":
                log.info("更新用户: {}", message.getData());
                break;
            case "DELETE":
                log.info("删除用户: {}", message.getOld());
                break;
            default:
                log.info("未知操作: {}", message);
                break;
        }
    }

    @Override
    public boolean supports(String tableName) {
        return "user".equals(tableName);
    }
}