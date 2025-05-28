package org.wj.letsrock.domain.article.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:14
 **/
@Data
public class ColumnArticleReq implements Serializable {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 专栏ID
     */
    private Long columnId;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 文章排序
     */
    private Integer sort;

    /**
     * 教程标题
     */
    private String shortTitle;

    /**
     * 阅读方式
     */
    private Integer read;
}
