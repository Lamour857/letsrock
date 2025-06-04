package org.wj.letsrock.infrastructure.task.comment;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-03-11:21
 **/
@Component
@ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "true")
@Slf4j
public class XxlJobArticleStatisticSyncTask extends SpringCommentStatisticSyncTask{
    @XxlJob("commentStatisticSyncHandler")
    public void execute(){
        log.info("XxlJob 启动评论统计同步任务");
        super.execute();
    }
}
