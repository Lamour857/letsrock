package org.wj.letsrock.domain.config.model.request;

import lombok.Data;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:06
 **/
@Data
public class GlobalConfigReq {
    // 配置项名称
    private String keywords;
    // 配置项值
    private String value;
    // 备注
    private String comment;
    // id
    private Long id;
}