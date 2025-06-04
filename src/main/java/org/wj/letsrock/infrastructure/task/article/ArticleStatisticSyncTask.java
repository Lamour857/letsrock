package org.wj.letsrock.infrastructure.task.article;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.infrastructure.task.TaskHandler;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-02-14:23
 **/
@Slf4j
public abstract class ArticleStatisticSyncTask implements TaskHandler {
    @Autowired
    private CacheService cacheService;

    @Autowired
    private ArticleRepository articleRepository;
    @Override
    public void execute() {
        try {
            // 1. 获取待更新的文章ID列表
            List<ImmutablePair<String, Double>> dirtyArticles =
                    cacheService.zTopNScore(CacheKey.DIRTY_ARTICLE_STATISTIC, 100);
            log.info( "待更新文章数：{}", dirtyArticles.size());

            if (CollectionUtils.isEmpty(dirtyArticles)) {
                return;
            }

            for (ImmutablePair<String, Double> pair : dirtyArticles) {
                Long articleId = Long.valueOf(pair.getLeft());
                try {
                    // 2. 获取缓存中的统计数据
                    Map<String, Long> statistics = cacheService.hGetAll(
                            CacheKey.articleStatisticInfo(articleId),
                            Long.class
                    );
                    if( statistics.isEmpty()) {
                        continue;
                    }
                    // 3. 更新数据库
                    articleRepository.updateArticleStatisticInfo(articleId, statistics);
                    // 4. 从脏数据集合中移除
                    cacheService.zRemove(CacheKey.DIRTY_ARTICLE_STATISTIC, articleId);

                    log.info("已同步文章数据: articleId={}, statistics={}",
                            articleId, statistics);
                } catch (Exception e) {
                    log.error("同步文章数据失败: articleId={}", articleId, e);
                }
            }
        } catch (Exception e) {
            log.error("文章同步失败", e);
        }finally {
            log.info("文章同步任务结束");
        }
    }
}
