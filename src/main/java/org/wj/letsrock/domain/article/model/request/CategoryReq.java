package org.wj.letsrock.domain.article.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-12:41
 **/
@Data
public class CategoryReq implements Serializable {
    /**
     * ID
     */
    private Long categoryId;

    /**
     * 类目名称
     */
    private String category;

    /**
     * 排序
     */
    private Integer rank;
}
