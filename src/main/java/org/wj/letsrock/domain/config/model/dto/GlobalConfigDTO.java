package org.wj.letsrock.domain.config.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-13:07
 **/
@Data
public class GlobalConfigDTO implements Serializable {
    // uid
    private static final long serialVersionUID = 1L;

    // id
    private Long id;
    // 配置项名称
    private String keywords;
    // 配置项值
    private String value;
    // 备注
    private String comment;
}