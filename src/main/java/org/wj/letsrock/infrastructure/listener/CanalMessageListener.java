package org.wj.letsrock.infrastructure.listener;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wj.letsrock.infrastructure.canal.CanalMessageDispatcher;
import org.wj.letsrock.infrastructure.event.CanalMessage;

@Slf4j
@Component
public class CanalMessageListener {
    
    private final CanalMessageDispatcher canalMessageDispatcher;
    private final Gson gson = new Gson();

    public CanalMessageListener(CanalMessageDispatcher canalMessageDispatcher) {
        this.canalMessageDispatcher = canalMessageDispatcher;
    }

    @RabbitListener(queues = "${canal.queue-name}")
    public void handleMessage(String jsonMessage) {  // 移除 Channel 和 tag 参数
        try {
            CanalMessage message = parseMessage(jsonMessage);
            canalMessageDispatcher.dispatch(message);
        } catch (Exception e) {
            log.error("处理Canal消息失败: {}", e.getMessage(), e);
            throw e;  // 抛出异常会触发消息重试
        }
    }

    private CanalMessage parseMessage(String json) {
        log.info("canal 消息到达:\n{}", json);
        return gson.fromJson(json, CanalMessage.class);
    }
}