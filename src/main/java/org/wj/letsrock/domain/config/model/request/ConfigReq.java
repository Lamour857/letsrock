package org.wj.letsrock.domain.config.model.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-26-12:47
 **/
@Data
public class ConfigReq implements Serializable {
    /**
     * ID
     */
    private Long configId;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 名称
     */
    private String name;

    /**
     * 图片链接
     */
    private String bannerUrl;

    /**
     * 跳转链接
     */
    private String jumpUrl;

    /**
     * 内容
     */
    private String content;

    /**
     * 排序
     */
    private Integer rank;

    /**
     * 标签
     */
    private String tags;
}
