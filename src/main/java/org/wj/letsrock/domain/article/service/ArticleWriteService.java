package org.wj.letsrock.domain.article.service;

import org.wj.letsrock.domain.article.model.request.ArticlePostReq;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-21-14:32
 **/
public interface ArticleWriteService {
    /**
     * 保存文章，当articleId存在时，表示更新记录； 不存在时，表示插入
     *
     * @param req
     * @return
     */
    Long saveArticle(ArticlePostReq req, Long userId);

    /**
     * 删除文章
     *
     * @param articleId   文章id
     * @param userId 执行操作的用户
     */
    void deleteArticle(Long articleId, Long userId);
}
