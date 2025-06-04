package org.wj.letsrock.infrastructure.task.article;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-14:25
 **/
@Component
@ConditionalOnProperty(prefix = "xxl.job", name = "enable", havingValue = "true")
@Slf4j
public class XxlJobArticleStatisticSyncTask extends ArticleStatisticSyncTask{

    @XxlJob("articleStatisticSyncHandler")
    @Override
    public void execute() {
         log.info("XxlJob 启动文章统计同步任务");
        super.execute();
    }
}
