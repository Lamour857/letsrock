package org.wj.letsrock.domain.article.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:39
 **/
@Data
public class SearchTagReq {
    // 标签名称
    private String tag;
    // 分页
    private Long pageNumber;
    private Long pageSize;
}

