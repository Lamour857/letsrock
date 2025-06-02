package org.wj.letsrock.domain.statistics.service;

import org.wj.letsrock.domain.user.model.dto.ArticleFootCountDTO;
import org.wj.letsrock.enums.notify.NotifyTypeEnum;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-19:23
 **/
public interface CountService {


    /**
     * 处理计数变动
     *
     * @param documentId 文章id
     * @param type 类型
     */
    void handleCount(NotifyTypeEnum type,Object content);


    /**
     * 获取评论点赞数量
     * @param commentId
     * @return
     */
    Long queryCommentPraiseCount(Long commentId);
    /**
     * 查询文章相关的统计信息
     *
     * @param id
     * @return 返回文章的 收藏、点赞、评论、阅读数
     */
    ArticleFootCountDTO queryArticleStatisticInfo(Long id);
}
