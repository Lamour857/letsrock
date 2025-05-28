package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:56
 **/
@Data
public class ArticleOtherDTO {
    // 文章的阅读类型
    private Integer readType;
    // 教程的翻页
    private ColumnArticleFlipDTO flip;
}
