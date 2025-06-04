package org.wj.letsrock.infrastructure.task.comment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-03-11:21
 **/
@Component
@ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "false", matchIfMissing = true)
@Slf4j
public class SpringCommentStatisticSyncTask extends CommentStatisticSyncTask{
    @Override
    @Scheduled (fixedRate = 5,timeUnit = TimeUnit.MINUTES)
    public void execute() {
        log.info("Spring Schedule 开始执行评论统计同步任务");
        super.execute();
    }
}
