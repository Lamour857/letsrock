package org.wj.letsrock.domain.article.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.article.model.entity.ArticlePraiseDO;
import org.wj.letsrock.domain.article.repository.ArticlePraiseRepository;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.article.service.ArticlePraiseService;
import org.wj.letsrock.enums.StatusEnum;
import org.wj.letsrock.infrastructure.utils.RateLimiter;
import org.wj.letsrock.utils.ExceptionUtil;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-05-26-16:01
 **/
@Service
@Slf4j
public class ArticlePraiseServiceImpl implements ArticlePraiseService {
    @Autowired
    private RateLimiter rateLimiter;
    @Autowired
    private CacheService cacheService;
    @Autowired
    private ArticleRepository articleRepository;
    @Autowired
    private ArticlePraiseRepository articlePraiseRepository;

    @Override
    public void handlePraise(Long articleId, Long userId) {
        // 1. 限流检查：同一用户对同一文章的点赞间隔不能小于1秒
        String rateKey = String.format("rate:praise:%d:%d", userId, articleId);
        if (!rateLimiter.tryAcquire(rateKey, 1)) {
            throw ExceptionUtil.of(StatusEnum.TOO_MANY_REQUESTS);
        }

        try {
            // 2. 使用Redis做缓存

            // 3. 使用分布式锁防止并发问题
            Boolean acquired = cacheService.acquire(CacheKey.lockPraiseKey(articleId, userId));

            if (acquired == null || !acquired) {
                throw ExceptionUtil.of(StatusEnum.PLEASE_WAIT);
            }

            try {
                // 4. 查询点赞状态
               ArticlePraiseDO existingPraise = articlePraiseRepository
                        .findByArticleIdAndUserId(articleId, userId);

                // 5. 更新点赞状态和计数
                if (existingPraise!=null) {
                    existingPraise.setPraiseState(existingPraise.getPraiseState()==1?0:1);
                    articlePraiseRepository.updateById(existingPraise);

                    if (existingPraise.getPraiseState()==1) {
                        articleRepository.incrementPraiseCount(articleId);
                    } else {
                        articleRepository.decrementPraiseCount(articleId);
                    }
                    cacheService.remove(CacheKey.articlePraiseKey(articleId, userId));
                } else {
                    // 添加点赞
                    ArticlePraiseDO praise = new ArticlePraiseDO();
                    praise.setArticleId(articleId);
                    praise.setUserId(userId);
                    articlePraiseRepository.save(praise);
                    articleRepository.incrementPraiseCount(articleId);
                    cacheService.put(CacheKey.articlePraiseKey(articleId, userId), praise);

                }
            } finally {
                // 释放锁
                cacheService.remove(CacheKey.lockPraiseKey(articleId, userId));
            }
        } catch (Exception e) {
            log.error("处理点赞失败", e);
            throw ExceptionUtil.of(StatusEnum.UNEXPECT_ERROR);
        }
    }
}
