package org.wj.letsrock.domain.statistics.service;

import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-19:23
 **/
public interface CountService {
    /**
     * 查询文章相关的统计信息
     *
     * @param id
     * @return 返回文章的 收藏、点赞、评论、阅读数
     */
    ArticleFootCountDTO queryArticleStatisticInfo(Long id);

    /**
     * 文章计数+1
     *
     * @param author 作者
     * @param articleId    文章
     * @return 计数器
     */
    void increaseArticleReadCount(Long author, Long articleId);

    /**
     * 获取评论点赞数量
     * @param commentId
     * @return
     */
    Long queryCommentPraiseCount(Long commentId);
}
