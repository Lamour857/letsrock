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
    private Integer  praiseCount;

    /**
     * 文章被阅读数
     */
    private Integer  readCount;

    /**
     * 文章被收藏数
     */
    private Integer  collectionCount;

    /**
     * 评论数
     */
    private Integer commentCount;

    public ArticleFootCountDTO() {
        praiseCount = 0;
        readCount = 0;
        collectionCount = 0;
        commentCount = 0;
    }
}
