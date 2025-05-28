package org.wj.letsrock.domain.statistics.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.article.repository.ArticleRepository;
import org.wj.letsrock.domain.cache.CacheKey;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;
import org.wj.letsrock.domain.user.repository.UserFootRepository;
import org.wj.letsrock.domain.statistics.service.CountService;

import java.util.Map;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-19:23
 **/
@Service
public class CountServiceImpl implements CountService {

    @Autowired
    private CacheService cacheService;
    @Autowired
    private ArticleRepository articleDao;
    @Autowired
    private UserFootRepository userFootDao;
    @Override
    public ArticleFootCountDTO queryArticleStatisticInfo(Long id) {
        Map<String, Integer> ans=cacheService.hGetAll(CacheKey.ARTICLE_STATISTIC_INFO + id,Integer.class);
        ArticleFootCountDTO info = new ArticleFootCountDTO();
        info.setPraiseCount((Integer) ans.getOrDefault(CacheKey.PRAISE_COUNT, 0));
        info.setCollectionCount((Integer)ans.getOrDefault(CacheKey.COLLECTION_COUNT, 0));
        info.setCommentCount((Integer)ans.getOrDefault(CacheKey.COMMENT_COUNT, 0));
        info.setReadCount((Integer)ans.getOrDefault(CacheKey.READ_COUNT, 0));
        return info;
    }

    @Override
    public void increaseArticleReadCount(Long author, Long articleId) {
        articleDao.increaseReadCount(articleId);
        // redis计数器 +1
        cacheService.hIncrement(CacheKey.ARTICLE_STATISTIC_INFO+articleId,CacheKey.READ_COUNT,1);
        cacheService.hIncrement(CacheKey.USER_STATISTIC_INFO+articleId,CacheKey.READ_COUNT,1);
    }

    @Override
    public Long queryCommentPraiseCount(Long commentId) {
        return userFootDao.countCommentPraise(commentId);
    }

}
