package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:22
 **/
@Data
public class ColumnFootCountDTO {
    /**
     * 专栏点赞数
     */
    private Integer praiseCount;

    /**
     * 专栏被阅读数
     */
    private Integer readCount;

    /**
     * 专栏被收藏数
     */
    private Integer collectionCount;

    /**
     * 专栏评论数
     */
    private Integer commentCount;

    /**
     * 专栏已更新的文章数
     */
    private Integer articleCount;

    /**
     * 专栏的文章总数
     */
    private Integer totalNums;

    public ColumnFootCountDTO() {
        praiseCount = 0;
        readCount = 0;
        collectionCount = 0;
        commentCount = 0;
        articleCount = 0;
        totalNums = 0;
    }
}
