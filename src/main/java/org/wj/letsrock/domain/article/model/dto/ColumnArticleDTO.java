package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-10:25
 **/
@Data
@Accessors(chain = true)
public class ColumnArticleDTO implements Serializable {
    private static final long serialVersionUID = 3646376715620165839L;

    /**
     * 唯一ID
     */
    private Long id;

    /**
     * 文章ID
     */
    private Long articleId;

    /**
     * 文章标题
     */
    private String title;

    /**
     * 教程名称
     */
    private String shortTitle;

    /**
     * 教程ID
     */
    private Long columnId;

    /**
     * 教程标题
     */
    private String column;

    /**
     * 教程封面
     */
    private String columnCover;

    /**
     * 文章排序
     */
    private Integer sort;

    /**
     * 创建时间
     */
    private Timestamp createTime;
}
