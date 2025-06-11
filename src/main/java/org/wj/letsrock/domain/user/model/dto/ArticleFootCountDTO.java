package org.wj.letsrock.domain.user.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-19-16:04
 **/
@Data
public class ArticleFootCountDTO {
    /**
     * 文章点赞数
     */
    private Long  praiseCount;

    /**
     * 文章被阅读数
     */
    private Long  readCount;

    /**
     * 文章被收藏数
     */
    private Long  collectionCount;

    /**
     * 评论数
     */
    private Long commentCount;

    public ArticleFootCountDTO() {
        praiseCount = 0L;
        readCount = 0L;
        collectionCount = 0L;
        commentCount = 0L;
    }
}
