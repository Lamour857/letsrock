package org.wj.letsrock.infrastructure.task.article;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-12:27
 **/
@Component
@ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "false", matchIfMissing = true)
@Slf4j
public class SpringArticleStatisticSyncTask extends ArticleStatisticSyncTask {
    /**
     * 每5分钟执行一次增量同步
     */
    @Override
    @Scheduled(fixedRate = 5,timeUnit = TimeUnit.MINUTES)
    public void execute() {
        log.info("Spring Schedule 开始执行文章统计同步任务");
        super.execute();
    }
}
