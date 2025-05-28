package org.wj.letsrock.domain.config.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:07
 **/
@Data
public class SearchGlobalConfigReq {
    // 配置项名称
    private String keywords;
    // 配置项值
    private String value;
    // 备注
    private String comment;
    // 分页
    private Long pageNumber;
    private Long pageSize;
}
