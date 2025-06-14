package org.wj.letsrock.infrastructure.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.wj.letsrock.application.notification.NotifyApplicationService;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.statistics.service.CountService;
import org.wj.letsrock.infrastructure.config.RabbitmqConfig;
import org.wj.letsrock.infrastructure.event.CanalMessage;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-23-13:45
 **/
@Component
@Slf4j
public class UserOperateListener {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private NotifyApplicationService notifyService;
    @Autowired
     private CountService  countService;


    @RabbitListener(queues = RabbitmqConfig.OPERATE_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void handleLikeMessage(NotifyMsgEvent<UserFootDO> message) {
        try {
            log.info("线程: {} \n处理RabbitMq消息: {}",Thread.currentThread().getName(),message);
            notifyService.saveArticleNotify(message.getContent(),message.getNotifyType());
        } catch (Exception e) {
            log.error("处理operate消息失败: {}", e.getMessage(), e);
            throw e;  // 抛出异常会触发消息重试
        }
        log.info("处理完成");
    }
    /**
     * 用户操作行为
     *
     * @param msgEvent
     */
    @EventListener(classes = NotifyMsgEvent.class)
    @Async("notifyEventExecutor")  // 指定使用自定义的线程池
    public <T> void notifyMsgListener(NotifyMsgEvent<T> msgEvent) {
        log.info("线程: {} 处理Spring消息: \n{}",Thread.currentThread().getName(),msgEvent);
        countService.handleCount(msgEvent.getNotifyType(),msgEvent.getContent());
        log.info("消息处理完成");
    }
}
