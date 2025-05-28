package org.wj.letsrock.domain.article.model.dto;

import lombok.Data;
import lombok.ToString;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-20-14:55
 **/
@Data
@ToString(callSuper = true)
public class YearArticleDTO {

    /**
     * 年份
     */
    private String year;

    /**
     * 文章数量
     */
    private Integer articleCount;
}