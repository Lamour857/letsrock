package org.wj.letsrock.infrastructure.canal.handler;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.user.model.entity.UserDO;
import org.wj.letsrock.infrastructure.canal.annotation.CanalTable;
import org.wj.letsrock.infrastructure.event.CanalMessage;
import org.wj.letsrock.infrastructure.persistence.es.model.UserDocument;
import org.wj.letsrock.infrastructure.persistence.es.repository.UserEsRepository;

@Slf4j
@Component
@CanalTable("user")
public class UserTableHandler implements CanalMessageHandler {
    
    @Autowired
    private UserEsRepository userEsRepository;
    
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void handle(CanalMessage message) {
        log.info("处理user变更: type={}, data={}", message.getType(), message.getData());
        String type = message.getType();
        
        try {
            switch (type) {
                case "INSERT":
                case "UPDATE":
                    message.getData().forEach(dataMap -> {
                        UserDO user = objectMapper.convertValue(dataMap, UserDO.class);
                        syncToEs(user);
                    });
                    break;
                    
                case "DELETE":
                    if (message.getOld() != null) {
                        message.getOld().forEach(oldData -> {
                            UserDO oldUser = objectMapper.convertValue(oldData, UserDO.class);
                            deleteFromEs(oldUser);
                        });
                    }
                    break;
                    
                default:
                    log.warn("未知的操作类型: {}", type);
            }
        } catch (Exception e) {
            log.error("同步用户数据失败: {}", e.getMessage(), e);
            throw new RuntimeException("同步用户数据失败", e);
        }
    }

    private void syncToEs(UserDO user) {
        if (user == null || user.getId() == null) {
            log.warn("用户数据为空，跳过同步");
            return;
        }

        UserDocument document = UserDocument.builder()
            .id(user.getId())
            .username(user.getUsername())
            .createTime(user.getCreateTime())
            .updateTime(user.getUpdateTime())
            .build();

        userEsRepository.save(document);
        log.info("同步用户到ES成功, userId: {}", user.getId());
    }

    private void deleteFromEs(UserDO user) {
        if (user == null || user.getId() == null) {
            log.warn("用户数据为空，跳过删除");
            return;
        }

        userEsRepository.deleteById(user.getId());
        log.info("从ES删除用户成功, userId: {}", user.getId());
    }

}