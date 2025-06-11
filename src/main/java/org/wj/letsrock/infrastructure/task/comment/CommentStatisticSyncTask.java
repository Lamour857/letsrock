package org.wj.letsrock.infrastructure.task.comment;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.beans.factory.annotation.Autowired;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.comment.repository.CommentRepository;
import org.wj.letsrock.infrastructure.task.TaskHandler;

import java.util.List;
import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-06-03-11:17
 **/
@Slf4j
public abstract class CommentStatisticSyncTask implements TaskHandler {
    @Autowired
    private CacheService cacheService;
    @Autowired
    private CommentRepository  commentRepository;

    @Override
    public void execute() {
        try {
            // 1. 获取待更新的评论ID列表
            List<ImmutablePair<String, Double>> dirtyArticles =
                    cacheService.zTopNScore(CacheKey.DIRTY_ARTICLE_STATISTIC, 100);
            log.info( "待更新评论数：{}", dirtyArticles.size());

            if (CollectionUtils.isEmpty(dirtyArticles)) {
                return;
            }

            for (ImmutablePair<String, Double> pair : dirtyArticles) {
                Long commentId = Long.valueOf(pair.getLeft());
                try {
                    // 2. 获取缓存中的统计数据
                    Map<String, Long> statistics = cacheService.hGetAll(
                            CacheKey.commentStatisticInfo(commentId),
                            Long.class
                    );
                    if( statistics.isEmpty()) {
                        continue;
                    }
                    // 3. 更新数据库
                    commentRepository.updateCommentStatisticInfo(commentId, statistics);
                    // 4. 从脏数据集合中移除
                    cacheService.zRemove(CacheKey.DIRTY_ARTICLE_STATISTIC, commentId);

                    log.info("已同步评论数据: commentId={}, statistics={}",
                            commentId, statistics);
                } catch (Exception e) {
                    log.error("同步评论数据失败: commentId={}", commentId, e);
                }
            }
        } catch (Exception e) {
            log.error("评论同步失败", e);
        }finally {
            log.info("评论同步任务结束");
        }
    }
}
