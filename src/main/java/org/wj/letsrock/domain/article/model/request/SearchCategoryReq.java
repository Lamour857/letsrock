package org.wj.letsrock.domain.article.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-25-12:41
 **/
@Data
public class SearchCategoryReq  {
    // 类目名称
    private String category;
    // 分页
    private Long pageNumber;
    private Long pageSize;
}
