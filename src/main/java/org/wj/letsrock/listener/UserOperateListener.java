package org.wj.letsrock.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.wj.letsrock.application.notification.NotifyApplicationService;
import org.wj.letsrock.domain.article.service.ArticlePraiseService;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.infrastructure.config.RabbitmqConfig;
import org.wj.letsrock.domain.comment.model.entity.CommentDO;
import org.wj.letsrock.infrastructure.event.NotifyMsgEvent;
import org.wj.letsrock.domain.user.model.entity.UserFootDO;
import org.wj.letsrock.domain.user.model.entity.UserRelationDO;

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
    private ArticlePraiseService articlePraiseService;

    @RabbitListener(queues = RabbitmqConfig.OPERATE_QUEUE,
            containerFactory = "rabbitListenerContainerFactory")
    public void handleLikeMessage(NotifyMsgEvent<UserFootDO> message) {
        log.info("线程: {} \n处理RabbitMq消息: {}",Thread.currentThread().getName(),message);
        notifyService.saveArticleNotify(message.getContent(),message.getNotifyType());
        log.info("处理完成");
    }
    /**
     * 用户操作行为，增加对应的积分
     *
     * @param msgEvent
     */
    @EventListener(classes = NotifyMsgEvent.class)
    @Async
    public <T> void notifyMsgListener(NotifyMsgEvent<T> msgEvent) {
        log.info("线程: {} \n处理Spring消息: {}",Thread.currentThread().getName(),msgEvent);
        CommentDO comment;
        UserRelationDO relation;
        UserFootDO foot;
        switch (msgEvent.getNotifyType()) {
            case COMMENT:
            case REPLY:
                 comment= (CommentDO) msgEvent.getContent();
                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + comment.getArticleId(), CacheKey.COMMENT_COUNT, 1);
                break;
            case DELETE_COMMENT:
            case DELETE_REPLY:
                comment = (CommentDO) msgEvent.getContent();
                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + comment.getArticleId(), CacheKey.COMMENT_COUNT, -1);
                break;
            case COLLECT:
                 foot= (UserFootDO) msgEvent.getContent();
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + foot.getDocumentUserId(), CacheKey.COLLECTION_COUNT, 1);
                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + foot.getDocumentId(), CacheKey.COLLECTION_COUNT, 1);
                break;
            case CANCEL_COLLECT:
                foot = (UserFootDO) msgEvent.getContent();
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + foot.getDocumentUserId(), CacheKey.COLLECTION_COUNT, -1);
                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + foot.getDocumentId(), CacheKey.COLLECTION_COUNT, -1);
                break;
            case PRAISE:
                foot = (UserFootDO) msgEvent.getContent();
//                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + foot.getDocumentUserId(), CacheKey.PRAISE_COUNT, 1);
//                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + foot.getDocumentId(), CacheKey.PRAISE_COUNT, 1);
                articlePraiseService.handlePraise(foot.getDocumentId(),foot.getUserId());
                break;
            case CANCEL_PRAISE:
                foot = (UserFootDO) msgEvent.getContent();
//                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + foot.getDocumentUserId(), CacheKey.PRAISE_COUNT, -1);
//                cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO + foot.getDocumentId(), CacheKey.PRAISE_COUNT, -1);
                articlePraiseService.handlePraise(foot.getDocumentId(),foot.getUserId());
                break;
            case FOLLOW:
                relation = (UserRelationDO) msgEvent.getContent();
                // 主用户粉丝数 + 1
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + relation.getUserId(), CacheKey.FANS_COUNT, 1);
                // 粉丝的关注数 + 1
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + relation.getFollowUserId(), CacheKey.FOLLOW_COUNT, 1);
                break;
            case CANCEL_FOLLOW:
                relation = (UserRelationDO) msgEvent.getContent();
                // 主用户粉丝数 + 1
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + relation.getUserId(), CacheKey.FANS_COUNT, -1);
                // 粉丝的关注数 + 1
                cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO + relation.getFollowUserId(), CacheKey.FOLLOW_COUNT, -1);
                break;
            default:
        }
        log.info("处理完成");
    }
}
