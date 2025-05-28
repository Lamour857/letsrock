package org.wj.letsrock.domain.article.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wj.letsrock.domain.cache.CacheService;
import org.wj.letsrock.domain.article.service.ArticleWhiteListService;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:25
 **/
@Service
public class ArticleWhiteListServiceImpl implements ArticleWhiteListService {
    private static final String ARTICLE_WHITE_LIST = "article_white_list";
    @Autowired
    private CacheService cacheService;
    /**
     * 判断作者是否再文章发布的白名单中；
     * 这个白名单主要是用于控制作者发文章之后是否需要进行审核
     *
     * @param authorId
     * @return
     */
    @Override
    public boolean authorInArticleWhiteList(Long userId) {
        return cacheService.sIsMember(ARTICLE_WHITE_LIST, userId);
    }
}
