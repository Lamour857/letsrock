package org.wj.letsrock.domain.article.model.request;

/**
 * @author wujia
 * @description: TODO
 * @createTime: 2025-04-27-10:47
 **/

import lombok.Data;

import java.io.Serializable;

@Data
public class TagReq implements Serializable {

    /**
     * ID
     */
    private Long tagId;

    /**
     * 标签名称
     */
    private String tag;

    /**
     * 类目ID
     */
    private Long categoryId;
}