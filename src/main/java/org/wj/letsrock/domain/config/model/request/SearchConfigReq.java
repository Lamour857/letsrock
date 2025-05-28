package org.wj.letsrock.domain.config.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:52
 **/
@Data
public class SearchConfigReq {
    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 分页
     */
    private Long pageNumber;
    private Long pageSize;
}
