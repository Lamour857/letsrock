package org.wj.letsrock.domain.article.service;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-19:24
 **/
public interface ArticleWhiteListService {
    /**
     * 判断作者是否再文章发布的白名单中；
     * 这个白名单主要是用于控制作者发文章之后是否需要进行审核
     *
     * @param authorId
     * @return
     */
    boolean authorInArticleWhiteList(Long userId);
}
